package com.pgalaxyp.fragmento.combat.state.runtime;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;

import java.util.Map;
import java.util.Objects;

public record EquippedSkillsRuntimeState(
        Map<SkillSlotId, SkillId> bySlot
) {
    public EquippedSkillsRuntimeState {
        bySlot = Map.copyOf(Objects.requireNonNullElseGet(bySlot, Map::of));
    }

    public static EquippedSkillsRuntimeState empty() {
        return new EquippedSkillsRuntimeState(Map.of());
    }

    public SkillId skillInSlot(SkillSlotId slot) {
        if (slot == null) {
            return null;
        }
        return bySlot.get(slot);
    }
}