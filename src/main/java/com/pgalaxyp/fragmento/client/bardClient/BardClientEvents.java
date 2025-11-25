package com.pgalaxyp.fragmento.client.bardClient;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.item.bard.weapon.AbstractWeapon;
import com.pgalaxyp.fragmento.network.LeftClickPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = Fragmento.MODID, value = Dist.CLIENT)
public class BardClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (shouldSkip(mc)) return;

        LocalPlayer player = mc.player;
        processBardAttack(mc, player);
    }

    private static void processBardAttack(Minecraft mc, Player player) {
        if (mc.screen != null) return;
        if (!mc.options.keyAttack.isDown()) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof AbstractWeapon)) return;
        if (mc.getConnection() == null) return;
        if (player.getCooldowns().isOnCooldown(stack.getItem())) return;

        boolean isUseKeyDown = mc.options.keyUse.isDown();
        mc.getConnection().send(new LeftClickPacket(InteractionHand.MAIN_HAND, isUseKeyDown));
    }

    private static boolean shouldSkip(Minecraft mc) {
        return mc.level == null || mc.player == null;
    }

    @SubscribeEvent
    public static void onInteractionKeyMapping(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (mc.screen != null) return;
        if (event.getKeyMapping() != mc.options.keyAttack) return;

        ItemStack stack = player.getMainHandItem();
        Item item = stack.getItem();
        if (!(item instanceof AbstractWeapon)) return;

        boolean isUseKeyDown = mc.options.keyUse.isDown();
        boolean onCooldown = player.getCooldowns().isOnCooldown(item);

        if (isUseKeyDown || onCooldown) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }
}