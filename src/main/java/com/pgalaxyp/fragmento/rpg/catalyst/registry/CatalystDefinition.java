package com.pgalaxyp.fragmento.rpg.catalyst.registry;

import com.pgalaxyp.fragmento.rpg.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public final class CatalystDefinition {

    private final CatalystFamilyId family;
    private final CatalystMatcher matcher;
    private final Map<SkillSlotId, SkillId> skillsBySlot;

    public CatalystDefinition(
            CatalystFamilyId family,
            CatalystMatcher matcher,
            Map<SkillSlotId, SkillId> skillsBySlot
    ) {
        if (family == null || matcher == null || skillsBySlot == null) {
            throw new IllegalArgumentException();
        }
        this.family = family;
        this.matcher = matcher;
        this.skillsBySlot = Map.copyOf(skillsBySlot);
    }

    public CatalystFamilyId family() {
        return family;
    }

    public boolean matches(ItemStack stack) {
        return matcher.matches(stack);
    }

    public Map<SkillSlotId, SkillId> skillsBySlot() {
        return skillsBySlot;
    }

    public interface CatalystMatcher {
        boolean matches(ItemStack stack);
    }
}