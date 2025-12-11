package com.pgalaxyp.fragmento.features.bard_class.instrument;

import com.pgalaxyp.fragmento.core.debug.ModLogger;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityBase;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityContext;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityResult;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilitySlot;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class InstrumentAbilityService {

    private InstrumentAbilityService() {
    }

    public static AbilityResult executeInstrumentAbility(ServerPlayer player, ItemStack stack, AbilitySlot slot, LivingEntity target) {
        if (!(stack.getItem() instanceof InstrumentBase instrument)) {
            ModLogger.log("Tried to execute instrument ability with non instrument item");
            return AbilityResult.failure();
        }

        if (!(player.level() instanceof ServerLevel level)) {
            ModLogger.log("Tried to execute instrument ability on non server level");
            return AbilityResult.failure();
        }

        if (InstrumentCooldownService.isOnCooldown(player, instrument)) {
            ModLogger.log("Ability blocked by cooldown slot=" + slot.name() + " player=" + player.getGameProfile().getName());
            return AbilityResult.failure();
        }

        AbilityBase ability = instrument.getAbility(slot.id());
        if (ability == null) {
            ModLogger.log("No ability found for slot id=" + slot.id() + " instrument=" + instrument);
            return AbilityResult.failure();
        }

        CastedSpiritBase.Mode mode = InstrumentStateService.resolveMode(stack, slot);

        AbilityContext ctx = new AbilityContext(level, player, stack, target, slot, mode);

        ModLogger.log("Executing ability slot=" + slot.name() + " mode=" + mode.name() + " player=" + player.getGameProfile().getName());

        AbilityResult result = ability.execute(ctx);

        if (result.success()) {
            InstrumentCooldownService.applyCooldown(player, instrument, result.cooldownTicks());
        }

        return result;
    }
}
