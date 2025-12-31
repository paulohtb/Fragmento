package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.snapshot.SkillSnapshot;

import java.util.Map;
import java.util.Objects;

public record SkillRuntimeState(
        Map<SkillId, CombatTime> cooldownEndsAt,
        long channelTargetEntityId,
        CombatTime channelEndsAt,
        boolean channelActive
) {
    public SkillRuntimeState {
        cooldownEndsAt = Map.copyOf(Objects.requireNonNullElseGet(cooldownEndsAt, Map::of));
        channelEndsAt = channelEndsAt == null ? CombatTime.ofTicks(0L) : channelEndsAt;
        channelTargetEntityId = Math.max(0L, channelTargetEntityId);
    }

    public static SkillRuntimeState initial() {
        return new SkillRuntimeState(
                Map.of(),
                0L,
                CombatTime.ofTicks(0L),
                false
        );
    }

    public CombatTime cooldownEnd(SkillId id) {
        return id == null ? null : cooldownEndsAt.get(id);
    }

    public SkillSnapshot snapshot() {
        return new SkillSnapshot(cooldownEndsAt);
    }
}