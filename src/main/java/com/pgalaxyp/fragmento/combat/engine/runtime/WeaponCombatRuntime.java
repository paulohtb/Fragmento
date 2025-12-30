package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.action.ActionDefinition;
import com.pgalaxyp.fragmento.combat.domain.action.ActionOutcome;
import com.pgalaxyp.fragmento.combat.domain.infusion.InfusedSkill;
import com.pgalaxyp.fragmento.combat.domain.input.WeaponInput;
import com.pgalaxyp.fragmento.combat.domain.input.WeaponInputType;
import com.pgalaxyp.fragmento.combat.domain.skill.CombatSkillKind;
import com.pgalaxyp.fragmento.combat.domain.skill.SkillDefinition;
import com.pgalaxyp.fragmento.combat.domain.skill.SkillCatalog;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.engine.rules.ComboInterruptRules;
import com.pgalaxyp.fragmento.combat.engine.rules.InfusedSkillRules;
import com.pgalaxyp.fragmento.combat.engine.rules.InfusionRules;
import com.pgalaxyp.fragmento.combat.engine.rules.WeaponFlowRules;
import com.pgalaxyp.fragmento.combat.engine.time.CombatClock;
import com.pgalaxyp.fragmento.combat.state.action.ActionPhase;
import com.pgalaxyp.fragmento.combat.state.session.WeaponSessionState;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboStep;

public final class WeaponCombatRuntime {

    private final CombatClock clock;
    private final WeaponRuntimeBindings bindings;
    private final WeaponRuntimeConfig config;
    private final SkillCatalog skillCatalog;
    private final InfusedSkillResolver infusedSkillResolver;

    private final WeaponFlowRules flowRules = new WeaponFlowRules();
    private final ComboInterruptRules interruptRules = new ComboInterruptRules();
    private final InfusionRules infusionRules = new InfusionRules();
    private final InfusedSkillRules infusedSkillRules = new InfusedSkillRules();

    private final WeaponSessionState session = new WeaponSessionState();

    private ComboDefinition combo;

    private int activeActionLocalId;
    private boolean awaitingHit;

    public WeaponCombatRuntime(
            CombatClock clock,
            WeaponRuntimeBindings bindings,
            WeaponRuntimeConfig config,
            SkillCatalog skillCatalog,
            InfusedSkillResolver infusedSkillResolver
    ) {
        this.clock = clock;
        this.bindings = bindings;
        this.config = config;
        this.skillCatalog = skillCatalog;
        this.infusedSkillResolver = infusedSkillResolver;
        this.activeActionLocalId = 0;
        this.awaitingHit = false;
    }

    public WeaponSessionState session() {
        return session;
    }

    public void bindCombo(ComboDefinition combo) {
        this.combo = combo;
        session.combo().bind(combo, config.maxGapBetweenHits());
    }

    public void tick() {
        CombatTime now = clock.now();

        session.action().tick(now);
        session.combo().tick(now);

        if (session.action().phase() == ActionPhase.RECOVERY) {
            ActionOutcome outcome = session.action().result().outcome();
            if (outcome == null) {
                outcome = ActionOutcome.MISSED;
            }
            finalizeActionOutcome(now, outcome);
            session.action().clear();
        }
    }

    public void onInput(WeaponInput input) {
        CombatTime now = clock.now();

        if (interruptRules.shouldInterruptCombo(input.type())) {
            session.combo().onInterrupted(now);
        }

        if (!flowRules.canAcceptInput(session, input)) {
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

        if (input.type() == WeaponInputType.SKILL_CANCEL) {
            if (bindings.skillEmitter() != null) {
                bindings.skillEmitter().cancelSkill(input.skillId());
            }
        }
    }

    public void onHitConfirmed(int actionLocalId) {
        if (!awaitingHit) {
            return;
        }
        if (actionLocalId != activeActionLocalId) {
            return;
        }

        CombatTime now = clock.now();
        session.action().endNow(ActionOutcome.HIT_CONFIRMED);
        session.combo().onHitConfirmed(now);
    }

    public void onActionFinishedNaturally(int actionLocalId) {
        if (actionLocalId != activeActionLocalId) {
            return;
        }
        session.action().endNow(ActionOutcome.MISSED);
    }

    public void interruptAll() {
        CombatTime now = clock.now();

        if (!session.action().isIdle()) {
            if (bindings.actionEmitter() != null && activeActionLocalId != 0) {
                bindings.actionEmitter().cancelAction(activeActionLocalId);
            }
            session.action().endNow(ActionOutcome.INTERRUPTED);
        }

        session.combo().onInterrupted(now);
        stopAwaitingHit();
    }

    private void handleSkillPress(int skillId, CombatTime now) {
        SkillDefinition def = skillCatalog != null ? skillCatalog.get(skillId) : null;

        if (def != null && def.combatKind() == CombatSkillKind.INFUSED) {
            InfusedSkill infused = infusedSkillResolver.resolve(skillId);
            if (infused != null && infusedSkillRules.canArm(session.infusion())) {
                infusedSkillRules.arm(
                        session.infusion(),
                        infused.infusionSpec(),
                        now
                );
            }
            return;
        }

        if (bindings.skillEmitter() != null) {
            bindings.skillEmitter().pressSkill(skillId);
        }
    }

    private void startNextAttack(CombatTime now) {
        ActionDefinition infused = infusionRules.resolveInfusedAction(session.infusion(), now);
        if (infused != null) {
            session.action().start(infused, now);
            activeActionLocalId = bindings.actionEmitter().emitAction(infused);
            startAwaitingHit(activeActionLocalId);
            return;
        }

        if (combo == null) {
            return;
        }

        ComboStep step = session.combo().currentStep();
        if (step == null) {
            return;
        }

        ActionDefinition action = step.action();

        session.combo().onActionStarted(now);
        session.action().start(action, now);

        if (bindings.actionEmitter() != null) {
            activeActionLocalId = bindings.actionEmitter().emitAction(action);
        } else {
            activeActionLocalId = 0;
        }

        startAwaitingHit(activeActionLocalId);
    }

    private void finalizeActionOutcome(CombatTime now, ActionOutcome outcome) {
        if (awaitingHit) {
            stopAwaitingHit();
        }

        if (outcome == ActionOutcome.HIT_CONFIRMED) {
            if (session.combo().isWindowOpen(now)) {
                session.combo().advanceAfterConfirmedHit();
            } else {
                session.combo().reset();
            }
            return;
        }

        session.combo().onMissOrNoHit(now);
    }

    private void startAwaitingHit(int actionLocalId) {
        awaitingHit = true;
        if (bindings.hitBus() != null) {
            bindings.hitBus().beginAwaitingHit(actionLocalId);
        }
    }

    private void stopAwaitingHit() {
        if (!awaitingHit) {
            return;
        }
        awaitingHit = false;
        if (bindings.hitBus() != null) {
            bindings.hitBus().endAwaitingHit(activeActionLocalId);
        }
    }
}