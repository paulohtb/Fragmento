package com.pgalaxyp.fragmento.features.bard_class.client.input;

import com.pgalaxyp.fragmento.core.debug.ModLogger;
import com.pgalaxyp.fragmento.core.network.packet.AbilityPacket;
import com.pgalaxyp.fragmento.core.util.RaycastUtil;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityBase;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilitySlot;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public final class BardAbilityClientController {

    private enum ChannelState {
        IDLE,
        CHANNELING,
        FINISHED,
        BLOCKED
    }

    private static ChannelState state = ChannelState.IDLE;
    private static int channelTicks;
    private static ItemStack itemRef;
    private static int currentTargetId;

    private static final int REQUIRED_CHANNEL_TICKS = 40;

    private BardAbilityClientController() {
    }

    public static void tick(Minecraft mc, LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        handleNormal(player, stack, instrument);
        handleSpecial(mc, player, stack, instrument);
    }

    private static void handleNormal(LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        if (!InstrumentKeybinds.NORMAL_ABILITY_USE.consumeClick()) {
            return;
        }

        if (player.getCooldowns().isOnCooldown(stack.getItem())) {
            ModLogger.input("normal_ability", "Blocked by cooldown for player=" + player.getGameProfile().getName());
            return;
        }

        AbilityBase ability = instrument.getAbility(AbilitySlot.BASIC.id());
        if (ability == null) {
            ModLogger.input("normal_ability", "No BASIC ability on instrument=" + stack.getItem());
            return;
        }

        double range = ability.getRange(stack);
        RaycastUtil.Result rc = RaycastUtil.perform(player, range);
        if (!rc.hasTarget()) {
            ModLogger.input("normal_ability", "No target in range for player=" + player.getGameProfile().getName());
            return;
        }

        int targetId = rc.target().getId();
        ModLogger.input("normal_ability", "Sending FINISH packet targetId=" + targetId);

        PacketDistributor.sendToServer(
                new AbilityPacket(AbilityPacket.Action.FINISH, AbilitySlot.BASIC.id(), targetId)
        );
    }

    private static void handleSpecial(Minecraft mc, LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        boolean down = mc.options.keyUse.isDown();

        switch (state) {
            case IDLE -> {
                if (!down) {
                    return;
                }

                if (player.getCooldowns().isOnCooldown(stack.getItem())) {
                    state = ChannelState.BLOCKED;
                    ModLogger.input("special_ability", "Blocked by cooldown at start for player=" + player.getGameProfile().getName());
                    return;
                }

                startChannel(player, stack, instrument);
            }

            case CHANNELING -> {
                if (!down) {
                    cancelChannel();
                    return;
                }

                if (isInterrupted(mc, player, stack)) {
                    cancelChannel();
                    return;
                }

                channelTicks++;
                if (channelTicks >= REQUIRED_CHANNEL_TICKS) {
                    finishChannel(player);
                }
            }

            case FINISHED, BLOCKED -> {
                if (player.getCooldowns().isOnCooldown(stack.getItem())) {
                    return;
                }

                if (down) {
                    startChannel(player, stack, instrument);
                } else {
                    state = ChannelState.IDLE;
                    channelTicks = 0;
                    itemRef = null;
                    currentTargetId = 0;
                }
            }
        }
    }

    private static void startChannel(LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        state = ChannelState.CHANNELING;
        channelTicks = 0;
        itemRef = stack.copy();

        AbilityBase ability = instrument.getAbility(AbilitySlot.SPECIAL.id());
        double range = ability != null ? ability.getRange(stack) : 10.0;
        RaycastUtil.Result rc = RaycastUtil.perform(player, range);

        int target = player.getId();
        if (rc.hasTarget() && rc.target() instanceof Player p) {
            target = p.getId();
        }

        currentTargetId = target;

        ModLogger.input("special_ability", "Starting channel with targetId=" + target);

        PacketDistributor.sendToServer(
                new AbilityPacket(AbilityPacket.Action.START, AbilitySlot.SPECIAL.id(), target)
        );
    }

    private static void finishChannel(LocalPlayer player) {
        state = ChannelState.FINISHED;
        channelTicks = 0;
        itemRef = null;

        ModLogger.input("special_ability", "Channel finished locally for player=" + player.getGameProfile().getName());
    }

    private static void cancelChannel() {
        ModLogger.input("special_ability", "Channel cancelled client side");

        PacketDistributor.sendToServer(
                new AbilityPacket(AbilityPacket.Action.CANCEL, AbilitySlot.SPECIAL.id(), currentTargetId)
        );

        state = ChannelState.IDLE;
        channelTicks = 0;
        itemRef = null;
        currentTargetId = 0;
    }

    private static boolean isInterrupted(Minecraft mc, LocalPlayer player, ItemStack stack) {
        if (itemRef == null || !ItemStack.isSameItemSameComponents(stack, itemRef)) {
            ModLogger.input("special_ability", "Interrupted by item change");
            return true;
        }

        if (mc.options.keyAttack.consumeClick()) {
            ModLogger.input("special_ability", "Interrupted by attack key");
            return true;
        }

        if (mc.screen != null) {
            ModLogger.input("special_ability", "Interrupted by opening screen");
            return true;
        }

        return false;
    }
}
