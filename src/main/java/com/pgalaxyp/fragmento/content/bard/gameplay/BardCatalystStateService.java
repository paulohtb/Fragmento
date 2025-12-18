package com.pgalaxyp.fragmento.content.bard.gameplay;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.system.charge.ChargeInstance;
import com.pgalaxyp.fragmento.system.charge.ChargeSystem;
import com.pgalaxyp.fragmento.system.skill.SkillMode;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class BardCatalystStateService {

    private BardCatalystStateService() {
    }

    public static SkillMode resolveMode(
            ChargeSystem charges,
            ItemStack stack,
            SkillSlot slot
    ) {
        if (slot == SkillSlot.SPECIAL) {
            return SkillMode.SPECIAL;
        }

        UUID instrumentId = BardCatalystIdService.getOrCreate(stack);
        if (instrumentId == null) {
            return SkillMode.BASIC;
        }

        ChargeInstance c = charges.getOrCreate(instrumentId, BardInstrumentConstants.MAX_CHARGE);
        if (c.isCharged()) {
            return SkillMode.CHARGED;
        }

        return SkillMode.BASIC;
    }
}