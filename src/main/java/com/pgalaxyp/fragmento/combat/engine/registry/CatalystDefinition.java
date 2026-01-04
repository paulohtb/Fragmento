package com.pgalaxyp.fragmento.combat.engine.registry;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.engine.profile.CombatProfile;
import net.minecraft.world.item.ItemStack;
import java.util.Map;

public final class CatalystDefinition {

    private final CatalystFamilyId family;
    private final CatalystMatcher matcher;
    private final Map<SkillSlotId, SkillId> skillsBySlot;
    private final CombatProfile profile;

    public CatalystDefinition(
            CatalystFamilyId family,
            CatalystMatcher matcher,
            Map<SkillSlotId, SkillId> skillsBySlot,
            CombatProfile profile
    ) {
        if (family == null || matcher == null || skillsBySlot == null || profile == null) {
            throw new IllegalArgumentException();
        }
        this.family = family;
        this.matcher = matcher;
        this.skillsBySlot = Map.copyOf(skillsBySlot);
        this.profile = profile;
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

    public CombatProfile profile() {
        return profile;
    }

    public interface CatalystMatcher {
        boolean matches(ItemStack stack);
    }
}