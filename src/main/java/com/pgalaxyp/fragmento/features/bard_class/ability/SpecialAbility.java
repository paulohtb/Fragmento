package com.pgalaxyp.fragmento.features.bard_class.ability;

import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import net.minecraft.server.level.ServerLevel;
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
    public AbilityResult execute(AbilityContext ctx) {
        S spirit = factory.apply(ctx.level());
        if (spirit == null) {
            return AbilityResult.failure();
        }

        spirit.summon(
                ctx.caster(),
                ctx.target(),
                ctx.level(),
                CastedSpiritBase.Mode.SPECIAL,
                ctx.instrumentStack()
        );

        return AbilityResult.successNoCooldown();
    }
}
