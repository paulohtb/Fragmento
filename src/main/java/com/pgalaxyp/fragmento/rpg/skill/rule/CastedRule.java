package com.pgalaxyp.fragmento.rpg.skill.rule;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg.skill.config.AbilityConfig;
import com.pgalaxyp.fragmento.rpg.state.runtime.AbilityState;
import com.pgalaxyp.fragmento.rpg.state.runtime.EquippedSkillsState;

import java.util.HashMap;
import java.util.Map;

public final class CastedRule {

    private final CooldownRule cooldownRule;
    private final AbilityConfig config;

    public CastedRule(CooldownRule cooldownRule, AbilityConfig config) {
        this.cooldownRule = cooldownRule;
        this.config = config;
    }

    public AbilityState apply(
            AbilityState state,
            EquippedSkillsState equipped,
            AbilityIntent intent,
            Time now
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

        Time endsAt = now.plus(config.castDuration(skillId));

        Map<SkillSlotId, AbilityState.CastRuntime> casting =
                new HashMap<>(state.casting());

        casting.put(slot, new AbilityState.CastRuntime(skillId, endsAt, false));

        return new AbilityState(
                state.cooldownEndsAt(),
                state.infusedArmed(),
                casting
        );
    }

    public AdvanceResult advanceAndCollectFinished(
            AbilityState state,
            Time now
    ) {
        if (state == null || now == null) {
            return new AdvanceResult(state, Map.of());
        }

        Map<SkillSlotId, AbilityState.CastRuntime> casting =
                new HashMap<>(state.casting());

        Map<SkillSlotId, SkillId> finished = new HashMap<>();

        boolean changed = false;

        for (var entry : state.casting().entrySet()) {
            AbilityState.CastRuntime cast = entry.getValue();
            if (!cast.ready() && now.isAfterOrEqual(cast.castEndsAt())) {
                casting.put(
                        entry.getKey(),
                        new AbilityState.CastRuntime(
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

        AbilityState next = state;
        if (changed) {
            next = new AbilityState(
                    state.cooldownEndsAt(),
                    state.infusedArmed(),
                    casting
            );
        }

        return new AdvanceResult(next, Map.copyOf(finished));
    }

    public AbilityState finish(
            AbilityState state,
            SkillSlotId slot,
            SkillId skillId,
            Time now
    ) {
        if (state == null || slot == null || skillId == null || now == null) {
            return state;
        }

        if (!state.casting().containsKey(slot)) {
            return state;
        }

        Map<SkillSlotId, AbilityState.CastRuntime> casting =
                new HashMap<>(state.casting());
        casting.remove(slot);

        AbilityState next = new AbilityState(
                state.cooldownEndsAt(),
                state.infusedArmed(),
                casting
        );

        return cooldownRule.startCooldown(next, skillId, config.cooldownDuration(skillId), now);
    }

    private AbilityState cancel(
            AbilityState state,
            SkillSlotId slot
    ) {
        if (!state.casting().containsKey(slot)) {
            return state;
        }

        Map<SkillSlotId, AbilityState.CastRuntime> casting =
                new HashMap<>(state.casting());
        casting.remove(slot);

        return new AbilityState(
                state.cooldownEndsAt(),
                state.infusedArmed(),
                casting
        );
    }

    public record AdvanceResult(
            AbilityState state,
            Map<SkillSlotId, SkillId> finished
    ) {}
}