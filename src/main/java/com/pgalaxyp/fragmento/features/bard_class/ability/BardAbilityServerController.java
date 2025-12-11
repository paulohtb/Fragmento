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

    private BardAbilityServerController() {
    }

    public static void handleAbilityPacket(ServerPlayer player, AbilityPacket packet) {
        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof InstrumentBase)) {
            ModLogger.network("ability", "Rejected ability packet because player is not holding an instrument player=" + player.getGameProfile().getName());
            return;
        }

        AbilitySlot slot = AbilitySlot.fromId(packet.abilityId());
        if (slot == null) {
            ModLogger.network("ability", "Rejected ability packet because of invalid ability id=" + packet.abilityId());
            return;
        }

        switch (packet.action()) {
            case START -> handleStart(player, stack, slot, packet);
            case FINISH -> handleFinish(player, stack, slot, packet);
            case CANCEL -> handleCancel(player, stack, slot);
            case TICK -> handleTick(player, slot);
        }
    }

    private static void handleStart(ServerPlayer player, ItemStack stack, AbilitySlot slot, AbilityPacket packet) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        LivingEntity target = resolveTarget(level, packet.targetId());
        if (target == null) {
            ModLogger.network("ability", "START with null target id=" + packet.targetId());
            return;
        }

        ModLogger.network("ability", "START slot=" + slot.name() + " player=" + player.getGameProfile().getName() + " targetId=" + target.getId());
        InstrumentAbilityService.executeInstrumentAbility(player, stack, slot, target);
    }

    private static void handleFinish(ServerPlayer player, ItemStack stack, AbilitySlot slot, AbilityPacket packet) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        LivingEntity target = resolveTarget(level, packet.targetId());
        if (target == null) {
            ModLogger.network("ability", "FINISH with null target id=" + packet.targetId());
            return;
        }

        ModLogger.network("ability", "FINISH slot=" + slot.name() + " player=" + player.getGameProfile().getName() + " targetId=" + target.getId());
        InstrumentAbilityService.executeInstrumentAbility(player, stack, slot, target);
    }

    private static void handleCancel(ServerPlayer player, ItemStack stack, AbilitySlot slot) {
        ModLogger.network("ability", "CANCEL slot=" + slot.name() + " player=" + player.getGameProfile().getName());
        SpiritQueryService.interruptSpecialSpirits(player);
    }

    private static void handleTick(ServerPlayer player, AbilitySlot slot) {
        ModLogger.network("ability", "TICK slot=" + slot.name() + " player=" + player.getGameProfile().getName());
    }

    private static LivingEntity resolveTarget(ServerLevel level, int targetId) {
        Entity e = level.getEntity(targetId);
        if (e instanceof LivingEntity living) {
            return living;
        }
        return null;
    }
}
