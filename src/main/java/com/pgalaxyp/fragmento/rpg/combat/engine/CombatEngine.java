package com.pgalaxyp.fragmento.rpg.combat.engine;

import com.pgalaxyp.fragmento.rpg.combat.rule.ComboResetRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboTimingRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.HoldLatchRule;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.lock.rule.ActionLockRule;
import com.pgalaxyp.fragmento.rpg.skill.rule.InfusedRule;
import com.pgalaxyp.fragmento.rpg.lock.rule.EquipGateRule;
import com.pgalaxyp.fragmento.rpg.lock.rule.LockGateRule;
import com.pgalaxyp.fragmento.rpg.combat.config.ComboConfig;
import com.pgalaxyp.fragmento.rpg.state.runtime.AbilityState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ComboState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ServerCombatState;

public final class CombatEngine {

    private final EquipGateRule equipGate;
    private final LockGateRule lockGate;
    private final ComboConfig comboConfig;
    private final ActionLockRule actionLockRule;
    private final ComboRule comboRule;
    private final ComboTimingRule timingRule;
    private final ComboResetRule resetRule;
    private final HoldLatchRule holdLatchRule;
    private final InfusedRule infusedRule;

    public CombatEngine(
            EquipGateRule equipGate,
            LockGateRule lockGate,
            ComboConfig comboConfig,
            ActionLockRule actionLockRule,
            ComboRule comboRule,
            ComboTimingRule timingRule,
            ComboResetRule resetRule,
            HoldLatchRule holdLatchRule,
            InfusedRule infusedRule
    ) {
        this.equipGate = equipGate;
        this.lockGate = lockGate;
        this.comboConfig = comboConfig;
        this.actionLockRule = actionLockRule;
        this.comboRule = comboRule;
        this.timingRule = timingRule;
        this.resetRule = resetRule;
        this.holdLatchRule = holdLatchRule;
        this.infusedRule = infusedRule;
    }

    public ServerCombatState applyAttack(ServerCombatState state, AttackIntent intent, Time now) {
        if (state == null || intent == null || now == null) {
            return state;
        }

        if (!equipGate.allowAttack(state.loadout(), intent)) {
            return state;
        }

        if (!lockGate.allowAttack(state.lock(), now)) {
            if (intent == AttackIntent.HOLD_START || intent == AttackIntent.HOLD_STOP) {
                ComboState next = comboRule.apply(state.combo(), intent, now);
                if (!next.equals(state.combo())) {
                    return state.withCombo(next);
                }
            }
            return state;
        }

        ComboState combo = state.combo();
        combo = resetRule.resetIfFinished(combo, comboConfig.maxSteps(), now);

        InfusedRule.ConsumeResult infused = null;
        if (intent == AttackIntent.CLICK || intent == AttackIntent.HOLD_START) {
            infused = infusedRule.consumeFirstArmed(state.abilities(), now);
        }

        ComboState next = comboRule.apply(combo, intent, now);
        boolean stepExecuted = next.stepIndex() != combo.stepIndex();

        if (stepExecuted) {
            next = timingRule.scheduleNext(next, comboConfig.stepDuration(), now);
            next = resetRule.resetIfFinished(next, comboConfig.maxSteps(), now);
        } else {
            next = holdLatchRule.advanceIfLatched(next, now);
            if (next.stepIndex() != combo.stepIndex()) {
                next = timingRule.scheduleNext(next, comboConfig.stepDuration(), now);
                next = resetRule.resetIfFinished(next, comboConfig.maxSteps(), now);
                stepExecuted = true;
            }
        }

        ServerCombatState out = state;
        if (!next.equals(state.combo())) {
            out = out.withCombo(next);
        }

        if (infused != null && infused.consumedSkillId() != null) {
            SkillId used = infused.consumedSkillId();
            AbilityState abilities =
                    infusedRule.startCooldownOnUse(infused.state(), used, now);

            out = out.withAbilities(abilities);
            out = out.withCombo(ComboState.idle());
            out = out.withLock(
                    actionLockRule.lock(
                            ActionKind.INFUSED_EXECUTE,
                            used,
                            comboConfig.actionLockDuration(),
                            now
                    )
            );
            return out;
        }

        if (stepExecuted) {
            out = out.withLock(
                    actionLockRule.lock(
                            ActionKind.COMBO_STEP,
                            null,
                            comboConfig.actionLockDuration(),
                            now
                    )
            );
        }

        return out;
    }

    public ServerCombatState tick(ServerCombatState state, Time now) {
        if (state == null || now == null) {
            return state;
        }

        ComboState next = holdLatchRule.advanceIfLatched(state.combo(), now);
        if (next.equals(state.combo())) {
            return state;
        }

        next = timingRule.scheduleNext(next, comboConfig.stepDuration(), now);
        next = resetRule.resetIfFinished(next, comboConfig.maxSteps(), now);

        ServerCombatState out = state.withCombo(next);
        out = out.withLock(
                actionLockRule.lock(
                        ActionKind.COMBO_STEP,
                        null,
                        comboConfig.actionLockDuration(),
                        now
                )
        );
        return out;
    }
}