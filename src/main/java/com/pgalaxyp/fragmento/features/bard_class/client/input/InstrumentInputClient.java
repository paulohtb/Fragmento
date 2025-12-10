package com.pgalaxyp.fragmento.features.bard_class.client.input;

import com.pgalaxyp.fragmento.core.util.RaycastUtil;
import com.pgalaxyp.fragmento.features.bard_class.ability.AbilityBase;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import com.pgalaxyp.fragmento.features.bard_class.network.packet.AbilityPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class InstrumentInputClient {

    private InstrumentInputClient() {}

    private static boolean channeling;
    private static int channelTicks;
    private static ItemStack channelItem;
    private static final int CHANNEL_TICKS_REQUIRED = 40;

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.screen != null) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof InstrumentBase instrument)) return;

        handleNormalAbility(player, stack, instrument);
        handleSpecialAbility(mc, player, stack, instrument);
    }

    private static void handleNormalAbility(LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        if (!InstrumentKeybinds.NORMAL_ABILITY_USE.consumeClick()) return;

        if (player.getCooldowns().isOnCooldown(stack.getItem())) {
            return;
        }

        AbilityBase ability = instrument.getAbility(0);
        if (ability == null) return;

        double range = ability.getRange(stack);
        RaycastUtil.Result rc = RaycastUtil.perform(player, range);
        if (!rc.hasTarget()) return;

        PacketDistributor.sendToServer(new AbilityPacket(0, rc.target().getId()));
    }

    private static void handleSpecialAbility(Minecraft mc, LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        if (player.getCooldowns().isOnCooldown(stack.getItem())) {
            cancelChanneling();
            return;
        }

        boolean useDown = mc.options.keyUse.isDown();

        if (!useDown) {
            cancelChanneling();
            return;
        }

        if (!channeling) {
            startChanneling(stack);
        }

        if (isChannelingInterrupted(mc, player, stack)) {
            cancelChanneling();
            return;
        }

        updateChanneling(mc, player, stack, instrument);
    }

    private static void startChanneling(ItemStack stack) {
        channeling = true;
        channelTicks = 0;
        channelItem = stack.copy();
    }

    private static void updateChanneling(Minecraft mc, LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        channelTicks++;

        if (channelTicks >= CHANNEL_TICKS_REQUIRED) {
            finishChanneling(player, stack, instrument);
            cancelChanneling();
        }
    }

    private static void finishChanneling(LocalPlayer player, ItemStack stack, InstrumentBase instrument) {
        AbilityBase ability = instrument.getAbility(1);
        double range = ability != null ? ability.getRange(stack) : 10.0;

        RaycastUtil.Result rc = RaycastUtil.perform(player, range);

        int targetId = player.getId();
        if (rc.hasTarget() && rc.target() instanceof Player targetPlayer) {
            targetId = targetPlayer.getId();
        }

        PacketDistributor.sendToServer(new AbilityPacket(1, targetId));
    }

    private static void cancelChanneling() {
        if (!channeling) return;
        channeling = false;
        channelTicks = 0;
        channelItem = null;
    }

    private static boolean isChannelingInterrupted(Minecraft mc, LocalPlayer player, ItemStack stack) {
        if (!ItemStack.isSameItemSameComponents(stack, channelItem)) return true;
        if (mc.options.keyAttack.consumeClick()) return true;
        return mc.screen != null;
    }
}
