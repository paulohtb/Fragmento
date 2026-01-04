package com.pgalaxyp.fragmento.combat.rule.combat;

import com.pgalaxyp.fragmento.combat.domain.action.ActionKind;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.ability.CastedRule;
import com.pgalaxyp.fragmento.combat.rule.ability.InfusedRule;
import com.pgalaxyp.fragmento.combat.rule.gate.EquipGateRule;
import com.pgalaxyp.fragmento.combat.rule.gate.LockGateRule;
import com.pgalaxyp.fragmento.combat.rule.port.AbilityConfig;
import com.pgalaxyp.fragmento.combat.state.runtime.ActionLockState;
import com.pgalaxyp.fragmento.combat.state.runtime.AbilityRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;

public final class AbilityEngine {

    private final EquipGateRule equipGate;
    private final LockGateRule lockGate;
    private final AbilityConfig config;
    private final ActionLockRule actionLockRule;
    private final InfusedRule infusedRule;
    private final CastedRule castedRule;

    public AbilityEngine(
            EquipGateRule equipGate,
            LockGateRule lockGate,
            AbilityConfig config,
            ActionLockRule actionLockRule,
            InfusedRule infusedRule,
            CastedRule castedRule
    ) {
        this.equipGate = equipGate;
        this.lockGate = lockGate;
        this.config = config;
        this.actionLockRule = actionLockRule;
        this.infusedRule = infusedRule;
        this.castedRule = castedRule;
    }

    public ServerCombatState apply(ServerCombatState state, AbilityIntent intent, CombatTime now) {
        if (state == null || intent == null || now == null) {
            return state;
        }

        if (!equipGate.allowAbility(state.loadout(), intent)) {
            return state;
        }

        if (!lockGate.allowAbility(state.lock(), intent, now)) {
            return state;
        }

        AbilityRuntimeState next = state.abilities();

        next = infusedRule.toggle(next, state.equippedSkills(), intent, now);
        next = castedRule.apply(next, state.equippedSkills(), intent, now);

        if (next == state.abilities()) {
            return state;
        }

        return state.withAbilities(next);
    }

    public ServerCombatState tick(ServerCombatState state, CombatTime now) {
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