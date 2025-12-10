package com.pgalaxyp.fragmento.features.bard_class.ability;

import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentChargeData;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase.Mode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.function.Function;

public record NormalAbility<S extends CastedSpiritBase>(
        Function<ServerLevel, S> basicFactory,
        Function<ServerLevel, S> chargedFactory,
        int basicCooldown,
        int chargedCooldown
) implements AbilityBase {

    @Override
    public double getRange(ItemStack stack) {
        return 12.0D;
    }

    @Override
    public int execute(ServerLevel level,
                       ServerPlayer player,
                       ItemStack stack,
                       LivingEntity target) {

        boolean charged = InstrumentChargeData.isCharged(stack);

        S spirit = charged
                ? chargedFactory.apply(level)
                : basicFactory.apply(level);

        if (spirit == null) {
            return -1;
        }

        Mode mode = charged ? Mode.CHARGED : Mode.BASIC;

        spirit.summon(player, target, level, mode);

        if (charged) {
            InstrumentChargeData.reset(stack);
        }

        return charged ? chargedCooldown : basicCooldown;
    }
}
