package com.pgalaxyp.fragmento.features.bard_class.ability;

import com.pgalaxyp.fragmento.core.debug.ModLogger;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase.Mode;
import net.minecraft.server.level.ServerLevel;
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
    public AbilityResult execute(AbilityContext ctx) {
        boolean charged = ctx.mode() == Mode.CHARGED;
        S spirit = charged
                ? chargedFactory.apply(ctx.level())
                : basicFactory.apply(ctx.level());

        String label = "NORMAL_" + (charged ? "CHARGED" : "BASIC");

        if (spirit == null) {
            ModLogger.abilityResult(label, false, 0);
            return AbilityResult.failure();
        }

        ModLogger.ability(label, ctx.caster(), ctx.target());
        spirit.summon(ctx.caster(), ctx.target(), ctx.level(), ctx.mode());

        int cooldown = charged ? chargedCooldown : basicCooldown;
        ModLogger.abilityResult(label, true, cooldown);

        return AbilityResult.successWithCooldown(cooldown);
    }
}
