package com.pgalaxyp.fragmento.rpg.combat.engine;

import com.pgalaxyp.fragmento.rpg.combat.config.ComboConfig;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboResetRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboTimingRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ExecutionGateRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.HoldLatchRule;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.lock.rule.ActionLockRule;
import com.pgalaxyp.fragmento.rpg.lock.rule.EquipGateRule;
import com.pgalaxyp.fragmento.rpg.lock.rule.LockGateRule;
import com.pgalaxyp.fragmento.rpg.skill.config.AbilityConfig;
import com.pgalaxyp.fragmento.rpg.skill.rule.InfusedRule;
import com.pgalaxyp.fragmento.rpg.state.runtime.AbilityState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ComboState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ExecutionKind;
import com.pgalaxyp.fragmento.rpg.state.runtime.ExecutionState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ServerCombatState;

public final class CombatEngine {

    private final EquipGateRule equipGate;
    private final LockGateRule lockGate;
    private final ExecutionGateRule executionGate;
    private final ComboConfig comboConfig;
    private final ActionLockRule actionLockRule;
    private final ComboRule comboRule;
    private final ComboTimingRule timingRule;
    private final ComboResetRule resetRule;
    private final HoldLatchRule holdLatchRule;
    private final InfusedRule infusedRule;
    private final AbilityConfig abilityConfig;

    public CombatEngine(
            EquipGateRule equipGate,
            LockGateRule lockGate,
            ExecutionGateRule executionGate,
            ComboConfig comboConfig,
            ActionLockRule actionLockRule,
            ComboRule comboRule,
            ComboTimingRule timingRule,
            ComboResetRule resetRule,
            HoldLatchRule holdLatchRule,
            InfusedRule infusedRule,
            AbilityConfig abilityConfig
    ) {
        this.equipGate = equipGate;
        this.lockGate = lockGate;
        this.executionGate = executionGate;
        this.comboConfig = comboConfig;
        this.actionLockRule = actionLockRule;
        this.comboRule = comboRule;
        this.timingRule = timingRule;
        this.resetRule = resetRule;
        this.holdLatchRule = holdLatchRule;
        this.infusedRule = infusedRule;
        this.abilityConfig = abilityConfig;
    }

    public ServerCombatState applyAttack(ServerCombatState state, AttackIntent intent, Time now) {
        if (state == null || intent == null || now == null) return state;
        if (!equipGate.allowAttack(state.loadout(), intent)) return state;
        if (!executionGate.allowAction(state.execution())) return state;
        if (!lockGate.allowAttack(state.lock(), now)) return state;

        InfusedRule.ConsumeResult infused = null;
        if (intent == AttackIntent.CLICK || intent == AttackIntent.HOLD_START) {
            infused = infusedRule.consumeFirstArmed(state.abilities(), now);
        }

        if (infused != null && infused.consumedSkillId() != null) {
            SkillId used = infused.consumedSkillId();
            AbilityState abilities = infusedRule.startCooldownOnUse(infused.state(), used, now);

            ExecutionState nextExec = startExecution(
                    state.execution(),
                    ExecutionKind.INFUSED_CUT,
                    now,
                    now.plus(abilityConfig.executionEntityLife(used))
            );

            return state
                    .withAbilities(abilities)
                    .withCombo(ComboState.idle())
                    .withExecution(nextExec)
                    .withLock(
                            actionLockRule.lock(
                                    ActionKind.INFUSED_EXECUTE,
                                    used,
                                    comboConfig.actionLockDuration(),
                                    now
                            )
                    );
        }

        ComboState combo = resetRule.resetIfFinished(state.combo(), comboConfig.maxSteps(), now);
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
        if (!next.equals(state.combo())) out = out.withCombo(next);

        if (stepExecuted) {
            ExecutionState nextExec = startExecution(
                    state.execution(),
                    ExecutionKind.COMBO_CUT,
                    now,
                    now.plus(comboConfig.executionEntityLife())
            );

            out = out
                    .withExecution(nextExec)
                    .withLock(
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
        if (state == null || now == null) return state;
        if (!executionGate.allowAction(state.execution())) return state;

        ComboState next = holdLatchRule.advanceIfLatched(state.combo(), now);
        if (next.equals(state.combo())) return state;

        next = timingRule.scheduleNext(next, comboConfig.stepDuration(), now);
        next = resetRule.resetIfFinished(next, comboConfig.maxSteps(), now);

        ExecutionState nextExec = startExecution(
                state.execution(),
                ExecutionKind.COMBO_CUT,
                now,
                now.plus(comboConfig.executionEntityLife())
        );

        return state
                .withCombo(next)
                .withExecution(nextExec)
                .withLock(
                        actionLockRule.lock(
                                ActionKind.COMBO_STEP,
                                null,
                                comboConfig.actionLockDuration(),
                                now
                        )
                );
    }

    private static ExecutionState startExecution(
            ExecutionState current,
            ExecutionKind kind,
            Time startedAt,
            Time expectedEndAt
    ) {
        ExecutionState cur = current != null ? current : ExecutionState.idle();
        long nextId = cur.execId() + 1L;

        return new ExecutionState(
                true,
                kind != null ? kind : ExecutionKind.NONE,
                nextId,
                startedAt != null ? startedAt : Time.ofTicks(0L),
                expectedEndAt != null ? expectedEndAt : Time.ofTicks(0L)
        );
    }
}