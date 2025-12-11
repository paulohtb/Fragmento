package com.pgalaxyp.fragmento.features.bard_class.ability;

import net.minecraft.world.item.ItemStack;

public interface AbilityBase {

    double getRange(ItemStack stack);

    AbilityResult execute(AbilityContext ctx);
}
