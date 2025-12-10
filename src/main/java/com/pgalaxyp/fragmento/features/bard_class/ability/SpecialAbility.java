package com.pgalaxyp.fragmento.features.bard_class.ability;

import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase.Mode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.function.Function;

public record SpecialAbility<S extends CastedSpiritBase>(
        Function<ServerLevel, S> factory,
        int cooldown
) implements AbilityBase {

    @Override
    public double getRange(ItemStack stack) {
        return 15.0D;
    }

    @Override
    public int execute(ServerLevel level,
                       ServerPlayer player,
                       ItemStack stack,
                       LivingEntity target) {

        S spirit = factory.apply(level);
        if (spirit == null) return -1;

        spirit.summon(player, target, level, Mode.SPECIAL);
        return cooldown;
    }
}
