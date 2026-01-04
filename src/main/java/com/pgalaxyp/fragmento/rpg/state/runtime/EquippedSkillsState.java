package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;

import java.util.Map;
import java.util.Objects;

public record EquippedSkillsState(
        Map<SkillSlotId, SkillId> bySlot
) {
    public EquippedSkillsState {
        bySlot = Map.copyOf(Objects.requireNonNullElseGet(bySlot, Map::of));
    }

    public static EquippedSkillsState empty() {
        return new EquippedSkillsState(Map.of());
    }

    public SkillId skillInSlot(SkillSlotId slot) {
        if (slot == null) {
            return null;
        }
        return bySlot.get(slot);
    }
}