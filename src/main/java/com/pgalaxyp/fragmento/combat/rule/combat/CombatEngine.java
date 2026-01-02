package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.ability.InfusedRule;
import com.pgalaxyp.fragmento.combat.rule.gate.EquipGateRule;
import com.pgalaxyp.fragmento.combat.rule.gate.LockGateRule;
import com.pgalaxyp.fragmento.combat.rule.port.ComboConfig;
import com.pgalaxyp.fragmento.combat.state.runtime.AbilityRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.ComboRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;

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

    public ServerCombatState applyAttack(
            ServerCombatState state,
            AttackIntent intent,
            CombatTime now
    ) {
        if (state == null || intent == null || now == null) {
            return state;
        }

        if (!equipGate.allowAttack(state.loadout(), intent)) {
            return state;
        }

        if (!lockGate.allowAttack(state.lock(), now)) {
            if (intent == AttackIntent.HOLD_START) {
                ComboRuntimeState latched = comboRule.apply(state.combo(), intent, now);
                if (latched != state.combo()) {
                    return state.withCombo(latched);
                }
            }
            return state;
        }

        ComboRuntimeState combo = state.combo();

        combo = resetRule.resetIfFinished(combo, comboConfig.maxSteps(), now);

        InfusedRule.ConsumeResult infused = null;
        if (intent == AttackIntent.CLICK || intent == AttackIntent.HOLD_START) {
            infused = infusedRule.consumeFirstArmed(state.abilities(), now);
        }

        ComboRuntimeState next = comboRule.apply(combo, intent, now);

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
        if (next != state.combo()) {
            out = out.withCombo(next);
        }

        if (infused != null && infused.consumedSkillId() != null) {
            AbilityRuntimeState abilities = infusedRule.startCooldownOnUse(infused.state(), infused.consumedSkillId(), now);
            out = out.withAbilities(abilities);
            out = out.withCombo(ComboRuntimeState.idle());
            out = out.withLock(actionLockRule.lock(comboConfig.actionLockDuration(), now));
            return out;
        }

        if (stepExecuted) {
            out = out.withLock(actionLockRule.lock(comboConfig.actionLockDuration(), now));
        }

        return out;
    }

    public ServerCombatState tick(
            ServerCombatState state,
            CombatTime now
    ) {
        if (state == null || now == null) {
            return state;
        }

        ComboRuntimeState next = holdLatchRule.advanceIfLatched(state.combo(), now);
        if (next == state.combo()) {
            return state;
        }

        next = timingRule.scheduleNext(next, comboConfig.stepDuration(), now);
        next = resetRule.resetIfFinished(next, comboConfig.maxSteps(), now);

        ServerCombatState out = state.withCombo(next);
        out = out.withLock(actionLockRule.lock(comboConfig.actionLockDuration(), now));
        return out;
    }
}