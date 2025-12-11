package com.pgalaxyp.fragmento.features.bard_class.instrument;

import com.pgalaxyp.fragmento.features.bard_class.ability.AbilitySlot;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import net.minecraft.world.item.ItemStack;

public final class InstrumentStateService {

    private InstrumentStateService() {
    }

    public static CastedSpiritBase.Mode resolveMode(ItemStack stack, AbilitySlot slot) {
        if (slot == AbilitySlot.SPECIAL) {
            return CastedSpiritBase.Mode.SPECIAL;
        }

        boolean charged = InstrumentChargeData.isCharged(stack);
        if (charged) {
            return CastedSpiritBase.Mode.CHARGED;
        }

        return CastedSpiritBase.Mode.BASIC;
    }
}
