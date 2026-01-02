package com.pgalaxyp.fragmento.combat.rule.ability;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.cooldown.CooldownRule;
import com.pgalaxyp.fragmento.combat.rule.port.AbilityConfig;
import com.pgalaxyp.fragmento.combat.state.runtime.AbilityRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.EquippedSkillsRuntimeState;

import java.util.HashMap;
import java.util.Map;

public final class CastedRule {

    private final CooldownRule cooldownRule;
    private final AbilityConfig config;

    public CastedRule(CooldownRule cooldownRule, AbilityConfig config) {
        this.cooldownRule = cooldownRule;
        this.config = config;
    }

    public AbilityRuntimeState apply(
            AbilityRuntimeState state,
            EquippedSkillsRuntimeState equipped,
            AbilityIntent intent,
            CombatTime now
    ) {
        if (state == null || equipped == null || intent == null || now == null) {
            return state;
        }

        SkillSlotId slot = intent.slot();

        if (intent.kind() == AbilityIntentKind.CANCEL) {
            return cancel(state, slot);
        }

        if (intent.kind() != AbilityIntentKind.PRESS) {
            return state;
        }

        if (state.casting().containsKey(slot)) {
            return state;
        }

        SkillId skillId = equipped.skillInSlot(slot);
        if (skillId == null) {
            return state;
        }

        if (!cooldownRule.ready(state, skillId, now)) {
            return state;
        }

        CombatTime endsAt = now.plus(config.castDuration(skillId));

        Map<SkillSlotId, AbilityRuntimeState.CastRuntime> casting =
                new HashMap<>(state.casting());

        casting.put(slot, new AbilityRuntimeState.CastRuntime(skillId, endsAt, false));

        return new AbilityRuntimeState(
                state.cooldownEndsAt(),
                state.infusedArmed(),
                casting
        );
    }

    public AdvanceResult advanceAndCollectFinished(
            AbilityRuntimeState state,
            CombatTime now
    ) {
        if (state == null || now == null) {
            return new AdvanceResult(state, Map.of());
        }

        Map<SkillSlotId, AbilityRuntimeState.CastRuntime> casting =
                new HashMap<>(state.casting());

        Map<SkillSlotId, SkillId> finished = new HashMap<>();

        boolean changed = false;

        for (var entry : state.casting().entrySet()) {
            AbilityRuntimeState.CastRuntime cast = entry.getValue();
            if (!cast.ready() && now.isAfterOrEqual(cast.castEndsAt())) {
                casting.put(
                        entry.getKey(),
                        new AbilityRuntimeState.CastRuntime(
                                cast.skillId(),
                                cast.castEndsAt(),
                                true
                        )
                );
                changed = true;
            }
            if (cast.ready() && now.isAfterOrEqual(cast.castEndsAt())) {
                finished.put(entry.getKey(), cast.skillId());
            }
        }

        AbilityRuntimeState next = state;
        if (changed) {
            next = new AbilityRuntimeState(
                    state.cooldownEndsAt(),
                    state.infusedArmed(),
                    casting
            );
        }

        return new AdvanceResult(next, Map.copyOf(finished));
    }

    public AbilityRuntimeState finish(
            AbilityRuntimeState state,
            SkillSlotId slot,
            SkillId skillId,
            CombatTime now
    ) {
        if (state == null || slot == null || skillId == null || now == null) {
            return state;
        }

        if (!state.casting().containsKey(slot)) {
            return state;
        }

        Map<SkillSlotId, AbilityRuntimeState.CastRuntime> casting =
                new HashMap<>(state.casting());
        casting.remove(slot);

        AbilityRuntimeState next = new AbilityRuntimeState(
                state.cooldownEndsAt(),
                state.infusedArmed(),
                casting
        );

        return cooldownRule.startCooldown(next, skillId, config.cooldownDuration(skillId), now);
    }

    private AbilityRuntimeState cancel(
            AbilityRuntimeState state,
            SkillSlotId slot
    ) {
        if (!state.casting().containsKey(slot)) {
            return state;
        }

        Map<SkillSlotId, AbilityRuntimeState.CastRuntime> casting =
                new HashMap<>(state.casting());
        casting.remove(slot);

        return new AbilityRuntimeState(
                state.cooldownEndsAt(),
                state.infusedArmed(),
                casting
        );
    }

    public record AdvanceResult(
            AbilityRuntimeState state,
            Map<SkillSlotId, SkillId> finished
    ) {}
}