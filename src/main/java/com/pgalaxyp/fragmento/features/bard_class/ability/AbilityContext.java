package com.pgalaxyp.fragmento.features.bard_class.ability;

import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record AbilityContext(
        ServerLevel level,
        ServerPlayer caster,
        ItemStack instrumentStack,
        LivingEntity target,
        AbilitySlot slot,
        CastedSpiritBase.Mode mode
) {
}
