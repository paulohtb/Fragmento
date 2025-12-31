package com.pgalaxyp.fragmento.combat.rule.runtime;

import com.pgalaxyp.fragmento.combat.content.registry.SkillCatalog;
import com.pgalaxyp.fragmento.combat.domain.action.ActionOutcome;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboStep;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;
import com.pgalaxyp.fragmento.combat.domain.input.WeaponInput;
import com.pgalaxyp.fragmento.combat.domain.input.WeaponInputType;
import com.pgalaxyp.fragmento.combat.domain.skill.CombatSkillKind;
import com.pgalaxyp.fragmento.combat.domain.skill.SkillDefinition;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.execution.InfusedSkillArmRule;
import com.pgalaxyp.fragmento.combat.rule.execution.InfusionRule;
import com.pgalaxyp.fragmento.combat.rule.port.InfusedSkillResolver;
import com.pgalaxyp.fragmento.combat.rule.port.WeaponRuntimeBindings;
import com.pgalaxyp.fragmento.combat.rule.port.WeaponRuntimeConfig;
import com.pgalaxyp.fragmento.combat.rule.validation.ComboInterruptRule;
import com.pgalaxyp.fragmento.combat.rule.validation.WeaponFlowRule;
import com.pgalaxyp.fragmento.combat.state.action.ActionPhase;
import com.pgalaxyp.fragmento.combat.state.combo.ComboProgressState;
import com.pgalaxyp.fragmento.combat.state.session.WeaponSessionState;

public final class WeaponCombatRuntime {

    private final WeaponRuntimeBindings bindings;
    private final WeaponRuntimeConfig config;
    private final SkillCatalog skillCatalog;
    private final InfusedSkillResolver infusedSkillResolver;

    private final WeaponFlowRule flowRule = new WeaponFlowRule();
    private final ComboInterruptRule interruptRule = new ComboInterruptRule();
    private final InfusionRule infusionRule = new InfusionRule();
    private final InfusedSkillArmRule infusedArmRule = new InfusedSkillArmRule();

    private final WeaponSessionState session = new WeaponSessionState();

    private ComboDefinition combo;

    private boolean awaitingHit;
    private int awaitingHitActionLocalId;

    public WeaponCombatRuntime(
            WeaponRuntimeBindings bindings,
            WeaponRuntimeConfig config,
            SkillCatalog skillCatalog,
            InfusedSkillResolver infusedSkillResolver
    ) {
        this.bindings = bindings;
        this.config = config;
        this.skillCatalog = skillCatalog;
        this.infusedSkillResolver = infusedSkillResolver;
    }

    public WeaponSessionState session() {
        return session;
    }

    public void bindCombo(ComboDefinition combo) {
        this.combo = combo;
        session.combo().bind(combo, config.maxGapBetweenHits());
    }

    public void tick(CombatTime now) {
        session.action().tick(now);
        session.combo().tick(now);

        if (session.action().phase() == ActionPhase.RECOVERY) {
            if (!session.action().result().resolved()) {
                session.action().endNow(ActionOutcome.MISSED);
            }

            ActionOutcome outcome = session.action().result().outcome();
            finalizeActionOutcome(now, outcome != null ? outcome : ActionOutcome.MISSED);
            session.action().clear();
        }
    }

    public void onInput(WeaponInput input, CombatTime now) {
        if (interruptRule.shouldInterruptCombo(input.type())) {
            session.combo().onInterrupted(now);
        }

        if (!flowRule.canAcceptInput(session, input)) {
            return;
        }

        if (input.type() == WeaponInputType.PRIMARY_ATTACK) {
            startNextAttack(now);
            return;
        }

        if (input.type() == WeaponInputType.SKILL_PRESS) {
            handleSkillPress(input.skillId(), now);
            return;
        }

        if (input.type() == WeaponInputType.SKILL_CANCEL && bindings.skillEmitter() != null) {
            bindings.skillEmitter().cancelSkill(input.skillId());
        }
    }

    public void onHitConfirmed(int actionLocalId, CombatTime now) {
        if (!awaitingHit) {
            return;
        }
        if (actionLocalId != awaitingHitActionLocalId) {
            return;
        }

        awaitingHit = false;
        awaitingHitActionLocalId = 0;

        if (bindings.hitBus() != null) {
            bindings.hitBus().endAwaitingHit(actionLocalId);
        }

        session.combo().onHitConfirmed(now);
        session.action().endNow(ActionOutcome.HIT_CONFIRMED);
    }

    public void interruptAll(CombatTime now) {
        endAwaitingHitIfAny();
        session.combo().onInterrupted(now);
        session.action().endNow(ActionOutcome.INTERRUPTED);
        session.action().clear();
    }

    private void startNextAttack(CombatTime now) {
        if (combo == null) {
            return;
        }

        if (session.combo().state() == ComboProgressState.WINDOW_OPEN && session.combo().isWindowOpen(now)) {
            session.combo().advanceAfterConfirmedHit();
        }

        if (session.combo().state() != ComboProgressState.READY) {
            return;
        }

        ComboStep step = session.combo().currentStep();
        if (step == null || step.action() == null) {
            return;
        }

        var infusion = infusionRule.resolve(session.infusion(), now);
        session.setInfusion(infusion.nextState());
        var action = infusion.infusedAction() != null ? infusion.infusedAction() : step.action();

        int actionLocalId = bindings.actionEmitter().emitAction(action);
        awaitingHit = true;
        awaitingHitActionLocalId = actionLocalId;

        session.combo().onActionStarted(now);
        session.action().start(action, now);

        if (bindings.hitBus() != null) {
            bindings.hitBus().beginAwaitingHit(actionLocalId);
        }
    }

    private void handleSkillPress(int skillId, CombatTime now) {
        SkillDefinition def = skillCatalog.get(skillId);
        if (def == null) {
            return;
        }

        if (def.combatKind() == CombatSkillKind.INFUSED) {
            InfusedSkill infused = infusedSkillResolver.resolve(skillId);
            if (infused != null && infusedArmRule.canArm(session.infusion())) {
                session.setInfusion(infusedArmRule.arm(session.infusion(), infused.infusionSpec(), now));
            }
            return;
        }

        if (bindings.skillEmitter() != null) {
            bindings.skillEmitter().pressSkill(skillId);
        }
    }

    private void finalizeActionOutcome(CombatTime now, ActionOutcome outcome) {
        if (outcome == ActionOutcome.HIT_CONFIRMED) {
            return;
        }
        session.combo().onMissOrNoHit(now);
        endAwaitingHitIfAny();
    }

    private void endAwaitingHitIfAny() {
        if (!awaitingHit) {
            awaitingHitActionLocalId = 0;
            return;
        }

        int actionLocalId = awaitingHitActionLocalId;
        awaitingHit = false;
        awaitingHitActionLocalId = 0;

        if (bindings.hitBus() != null && actionLocalId != 0) {
            bindings.hitBus().endAwaitingHit(actionLocalId);
        }
    }
}