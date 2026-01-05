package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;

import java.util.Map;

public record EquippedSkillsState(
        Map<SkillSlotId, SkillId> bySlot
) {

    public static EquippedSkillsState empty() {
        return new EquippedSkillsState(Map.of());
    }

    public SkillId skillInSlot(SkillSlotId slot) {
        return bySlot.get(slot);
    }
}