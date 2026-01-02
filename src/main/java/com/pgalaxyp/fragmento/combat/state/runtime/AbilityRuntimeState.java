package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.snapshot.AbilitySnapshot;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record AbilityRuntimeState(
        Map<SkillId, CombatTime> cooldownEndsAt,
        Map<SkillSlotId, SkillId> infusedArmed,
        Map<SkillSlotId, CastRuntime> casting
) {

    public record CastRuntime(
            SkillId skillId,
            CombatTime castEndsAt,
            boolean ready
    ) {
        public CastRuntime {
            if (skillId == null || castEndsAt == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public AbilityRuntimeState {
        cooldownEndsAt = Map.copyOf(Objects.requireNonNullElseGet(cooldownEndsAt, Map::of));
        infusedArmed = Map.copyOf(Objects.requireNonNullElseGet(infusedArmed, Map::of));
        casting = Map.copyOf(Objects.requireNonNullElseGet(casting, Map::of));
    }

    public static AbilityRuntimeState initial() {
        return new AbilityRuntimeState(Map.of(), Map.of(), Map.of());
    }

    public AbilitySnapshot snapshot() {
        return new AbilitySnapshot(
                cooldownEndsAt,
                infusedArmed,
                casting.entrySet().stream()
                        .collect(Collectors.toMap(
                                Map.Entry::getKey,
                                e -> new AbilitySnapshot.CastState(
                                        e.getValue().castEndsAt(),
                                        e.getValue().ready()
                                )
                        ))
        );
    }
}