package com.pgalaxyp.fragmento.rpg_old.state.runtime;

import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.AbilitySnapshot;

import java.util.HashMap;
import java.util.Map;

public record AbilityState(
        Map<SkillId, Time> cooldownEndsAt,
        Map<SkillSlotId, SkillId> infusedArmed,
        Map<SkillSlotId, CastRuntime> casting
) {

    public record CastRuntime(
            SkillId skillId,
            Time castEndsAt,
            boolean ready
    ) {}

    public static AbilityState initial() {
        return new AbilityState(
                Map.of(),
                Map.of(),
                Map.of()
        );
    }

    public AbilitySnapshot toSnapshot() {
        Map<SkillSlotId, AbilitySnapshot.CastState> castSnap = new HashMap<>();
        for (var e : casting.entrySet()) {
            CastRuntime v = e.getValue();
            castSnap.put(
                    e.getKey(),
                    new AbilitySnapshot.CastState(
                            v.castEndsAt(),
                            v.ready()
                    )
            );
        }

        return new AbilitySnapshot(
                cooldownEndsAt,
                infusedArmed,
                castSnap
        );
    }
}