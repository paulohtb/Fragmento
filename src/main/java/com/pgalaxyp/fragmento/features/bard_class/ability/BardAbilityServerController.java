package com.pgalaxyp.fragmento.features.bard_class.ability;

import com.pgalaxyp.fragmento.core.debug.ModLogger;
import com.pgalaxyp.fragmento.core.network.packet.AbilityPacket;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentAbilityService;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.SpiritQueryService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class BardAbilityServerController {

    private BardAbilityServerController() {}

    public static void handleAbilityPacket(ServerPlayer player, AbilityPacket packet) {
        AbilitySlot slot = AbilitySlot.fromId(packet.abilityId());
        if (slot == null) return;

        if (packet.action() == AbilityPacket.Action.CANCEL) {
            SpiritQueryService.interruptSpecialSpirits(player);
            return;
        }

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof InstrumentBase)) {
            return;
        }

        switch (packet.action()) {
            case START -> handleStart(player, stack, slot, packet);
            case FINISH -> handleFinish(player, stack, slot, packet);
            case TICK -> {}
            case CANCEL -> {}
        }
    }

    private static void handleStart(ServerPlayer player, ItemStack stack, AbilitySlot slot, AbilityPacket packet) {
        if (!(player.level() instanceof ServerLevel level)) return;
        LivingEntity target = resolveTarget(level, packet.targetId());
        if (target == null) return;
        InstrumentAbilityService.executeInstrumentAbility(player, stack, slot, target);
    }

    private static void handleFinish(ServerPlayer player, ItemStack stack, AbilitySlot slot, AbilityPacket packet) {
        if (!(player.level() instanceof ServerLevel level)) return;
        LivingEntity target = resolveTarget(level, packet.targetId());
        if (target == null) return;
        InstrumentAbilityService.executeInstrumentAbility(player, stack, slot, target);
    }

    private static LivingEntity resolveTarget(ServerLevel level, int targetId) {
        Entity e = level.getEntity(targetId);
        return e instanceof LivingEntity l ? l : null;
    }
}
