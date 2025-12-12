package com.pgalaxyp.fragmento.features.bard_class.instrument;

import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityBase;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityContext;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityResult;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilitySlot;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class InstrumentAbilityService {

    private InstrumentAbilityService() {}

    public static AbilityResult executeInstrumentAbility(ServerPlayer player, ItemStack stack, AbilitySlot slot, LivingEntity target) {
        if (!(stack.getItem() instanceof InstrumentBase instrument)) {
            return AbilityResult.failure();
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return AbilityResult.failure();
        }

        if (InstrumentCooldownService.isOnCooldown(player, instrument)) {
            return AbilityResult.failure();
        }

        AbilityBase ability = instrument.getAbility(slot.id());
        if (ability == null) {
            return AbilityResult.failure();
        }

        LivingEntity finalTarget = target;

        if (slot == AbilitySlot.SPECIAL) {
            if (!(finalTarget instanceof Player)) {
                finalTarget = player;
            }
        }

        CastedSpiritBase.Mode mode = InstrumentStateService.resolveMode(stack, slot);
        AbilityContext ctx = new AbilityContext(level, player, stack, finalTarget, slot, mode);

        AbilityResult result = ability.execute(ctx);

        if (result.success()) {
            InstrumentCooldownService.applyCooldown(player, instrument, result.cooldownTicks());
        }

        return result;
    }
}
