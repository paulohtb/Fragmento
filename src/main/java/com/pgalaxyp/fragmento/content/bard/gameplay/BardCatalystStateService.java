package com.pgalaxyp.fragmento.content.bard.gameplay;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardChargeData;
import net.minecraft.world.item.ItemStack;

public final class BardCatalystStateService {

    private BardCatalystStateService() {
    }

    public static com.pgalaxyp.fragmento.gameplay.skill.SkillMode resolveMode(
            ItemStack stack,
            com.pgalaxyp.fragmento.gameplay.skill.SkillSlot slot
    ) {
        if (slot == com.pgalaxyp.fragmento.gameplay.skill.SkillSlot.SPECIAL) {
            return com.pgalaxyp.fragmento.gameplay.skill.SkillMode.SPECIAL;
        }

        if (BardChargeData.isCharged(stack)) {
            return com.pgalaxyp.fragmento.gameplay.skill.SkillMode.CHARGED;
        }

        return com.pgalaxyp.fragmento.gameplay.skill.SkillMode.BASIC;
    }
}
