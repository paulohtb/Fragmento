package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg.state.snapshot.AbilitySnapshot;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public record AbilityState(
        Map<SkillId, Time> cooldownEndsAt,
        Map<SkillSlotId, SkillId> infusedArmed,
        Map<SkillSlotId, CastRuntime> casting
) {

    public record CastRuntime(
            SkillId skillId,
            Time castEndsAt,
            boolean ready
    ) {
        public CastRuntime {
            if (skillId == null || castEndsAt == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public AbilityState {
        cooldownEndsAt = Map.copyOf(Objects.requireNonNullElseGet(cooldownEndsAt, Map::of));
        infusedArmed = Map.copyOf(Objects.requireNonNullElseGet(infusedArmed, Map::of));
        casting = Map.copyOf(Objects.requireNonNullElseGet(casting, Map::of));
    }

    public static AbilityState initial() {
        return new AbilityState(Map.of(), Map.of(), Map.of());
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