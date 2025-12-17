package com.pgalaxyp.fragmento.content.bard.gameplay;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardChargeData;
import com.pgalaxyp.fragmento.system.skill.SkillMode;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import net.minecraft.world.item.ItemStack;

public final class BardCatalystStateService {

    private BardCatalystStateService() {
    }

    public static SkillMode resolveMode(
            ItemStack stack,
            SkillSlot slot
    ) {
        if (slot == SkillSlot.SPECIAL) {
            return SkillMode.SPECIAL;
        }

        if (BardChargeData.isCharged(stack)) {
            return SkillMode.CHARGED;
        }

        return SkillMode.BASIC;
    }
}
