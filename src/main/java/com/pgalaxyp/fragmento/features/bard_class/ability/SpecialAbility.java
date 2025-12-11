package com.pgalaxyp.fragmento.features.bard_class.ability;

import com.pgalaxyp.fragmento.core.debug.ModLogger;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase.Mode;
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
            ModLogger.abilityResult("SPECIAL", false, 0);
            return AbilityResult.failure();
        }

        ModLogger.ability("SPECIAL", ctx.caster(), ctx.target());
        spirit.summon(ctx.caster(), ctx.target(), ctx.level(), Mode.SPECIAL);

        ModLogger.abilityResult("SPECIAL", true, 0);
        return AbilityResult.successNoCooldown();
    }
}
