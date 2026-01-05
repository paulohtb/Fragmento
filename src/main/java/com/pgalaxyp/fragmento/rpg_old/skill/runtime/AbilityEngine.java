package com.pgalaxyp.fragmento.rpg_old.skill.runtime;

import com.pgalaxyp.fragmento.rpg_old.combat.rule.ExecutionGateRule;
import com.pgalaxyp.fragmento.rpg_old.lock.rule.ActionLockRule;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg_old.lock.rule.EquipGateRule;
import com.pgalaxyp.fragmento.rpg_old.lock.rule.LockGateRule;
import com.pgalaxyp.fragmento.rpg_old.skill.config.AbilityConfig;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.ActionLockState;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.AbilityState;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.ServerCombatState;
import com.pgalaxyp.fragmento.rpg_old.skill.rule.CastedRule;
import com.pgalaxyp.fragmento.rpg_old.skill.rule.InfusedRule;

public final class AbilityEngine {

    private final EquipGateRule equipGate;
    private final LockGateRule lockGate;
    private final ExecutionGateRule executionGate;
    private final AbilityConfig config;
    private final ActionLockRule actionLockRule;
    private final InfusedRule infusedRule;
    private final CastedRule castedRule;

    public AbilityEngine(
            EquipGateRule equipGate,
            LockGateRule lockGate,
            ExecutionGateRule executionGate,
            AbilityConfig config,
            ActionLockRule actionLockRule,
            InfusedRule infusedRule,
            CastedRule castedRule
    ) {
        this.equipGate = equipGate;
        this.lockGate = lockGate;
        this.executionGate = executionGate;
        this.config = config;
        this.actionLockRule = actionLockRule;
        this.infusedRule = infusedRule;
        this.castedRule = castedRule;
    }

    public ServerCombatState apply(ServerCombatState state, AbilityIntent intent, Time now) {
        if (state == null || intent == null || now == null) {
            return state;
        }

        if (!equipGate.allowAbility(state.loadout(), intent)) {
            return state;
        }

        if (!executionGate.allowAction(state.execution())) {
            return state;
        }

        if (!lockGate.allowAbility(state.lock(), intent, now)) {
            return state;
        }

        AbilityState next = state.abilities();

        next = infusedRule.toggle(next, state.equippedSkills(), intent, now);
        next = castedRule.apply(next, state.equippedSkills(), intent, now);

        if (next == state.abilities()) {
            return state;
        }

        return state.withAbilities(next);
    }

    public ServerCombatState tick(ServerCombatState state, Time now) {
        if (state == null || now == null) {
            return state;
        }

        CastedRule.AdvanceResult advanced =
                castedRule.advanceAndCollectFinished(state.abilities(), now);

        ServerCombatState out = state;
        if (advanced.state() != state.abilities()) {
            out = out.withAbilities(advanced.state());
        }

        if (advanced.finished().isEmpty()) {
            return out;
        }

        for (var entry : advanced.finished().entrySet()) {
            SkillId skillId = entry.getValue();

            out = out.withAbilities(
                    castedRule.finish(
                            out.abilities(),
                            entry.getKey(),
                            skillId,
                            now
                    )
            );

            ActionLockState lock =
                    actionLockRule.lock(
                            ActionKind.CAST_FINISH,
                            skillId,
                            config.actionLockDuration(skillId),
                            now
                    );

            out = out.withLock(lock);
        }

        return out;
    }
}