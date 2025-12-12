package com.pgalaxyp.fragmento.features.bard_class.client.input;

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
        FINISHED
    }

    private static ChannelState state = ChannelState.IDLE;
    private static int channelTicks;
    private static ItemStack itemRef;
    private static int currentTargetId;

    private static final int REQUIRED_CHANNEL_TICKS = 40;

    private BardAbilityClientController() {}

    public static void tick(Minecraft mc, LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        handleNormal(player, stack, instrument);
        handleSpecial(mc, player, stack, instrument);
    }

    private static void handleNormal(LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        if (instrument == null) return;
        if (!InstrumentKeybinds.NORMAL_ABILITY_USE.consumeClick()) return;

        AbilityBase ability = instrument.getAbility(AbilitySlot.BASIC.id());
        if (ability == null) return;

        RaycastUtil.Result rc = RaycastUtil.perform(player, ability.getRange(stack));
        if (!rc.hasTarget()) return;

        PacketDistributor.sendToServer(
                new AbilityPacket(AbilityPacket.Action.FINISH, AbilitySlot.BASIC.id(), rc.target().getId())
        );
    }

    private static void handleSpecial(Minecraft mc, LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        boolean down = mc.options.keyUse.isDown();

        if (state == ChannelState.CHANNELING && instrument == null) {
            cancelChannel();
            return;
        }

        switch (state) {
            case IDLE -> {
                if (!down || instrument == null) return;
                startChannel(player, stack, instrument);
            }

            case CHANNELING -> {
                if (!down) {
                    cancelChannel();
                    return;
                }

                if (!ItemStack.isSameItemSameComponents(stack, itemRef)) {
                    cancelChannel();
                    return;
                }

                channelTicks++;
                if (channelTicks >= REQUIRED_CHANNEL_TICKS) {
                    finishChannel();
                }
            }

            case FINISHED -> {
                if (!down) {
                    state = ChannelState.IDLE;
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

        PacketDistributor.sendToServer(
                new AbilityPacket(AbilityPacket.Action.START, AbilitySlot.SPECIAL.id(), target)
        );
    }

    private static void finishChannel() {
        state = ChannelState.FINISHED;
        channelTicks = 0;
        itemRef = null;
    }

    private static void cancelChannel() {
        PacketDistributor.sendToServer(
                new AbilityPacket(AbilityPacket.Action.CANCEL, AbilitySlot.SPECIAL.id(), currentTargetId)
        );

        state = ChannelState.IDLE;
        channelTicks = 0;
        itemRef = null;
        currentTargetId = 0;
    }
}
