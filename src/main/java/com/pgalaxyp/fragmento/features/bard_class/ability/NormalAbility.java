package com.pgalaxyp.fragmento.features.bard_class.ability;

import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.function.Function;
import static com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentChargeData.isCharged;

public record NormalAbility<S extends CastedSpiritBase>(
        Function<ServerLevel, S> basicFactory,
        Function<ServerLevel, S> chargedFactory,
        double basicRange,
        double chargedRange
) {

    public double getRange(ItemStack stack) {
        return isCharged(stack) ? chargedRange : basicRange;
    }

    public boolean execute(ServerLevel level, ServerPlayer player, ItemStack stack, LivingEntity target, boolean charged) {

        S spirit = charged ? chargedFactory.apply(level) : basicFactory.apply(level);

        if (spirit == null) return false;
        spirit.summon(player, target, level, charged);

        return true;
    }
}
