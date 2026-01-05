package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;

import java.util.Map;
import java.util.Objects;

public record EquippedSkillsSnapshot(
        Map<SkillSlotId, SkillId> bySlot
) {
    public EquippedSkillsSnapshot {
        bySlot = Map.copyOf(Objects.requireNonNullElseGet(bySlot, Map::of));
    }

    public static EquippedSkillsSnapshot empty() {
        return new EquippedSkillsSnapshot(Map.of());
    }

    public SkillId skillInSlot(SkillSlotId slot) {
        if (slot == null) return null;
        return bySlot.get(slot);
    }
}