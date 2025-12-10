package com.pgalaxyp.fragmento.features.bard_class.ability;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface AbilityBase {

    double getRange(ItemStack stack);

    int execute(ServerLevel level,
                ServerPlayer player,
                ItemStack stack,
                LivingEntity target);
}
