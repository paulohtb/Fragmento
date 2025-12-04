package com.pgalaxyp.fragmento.feature.bard.client.input;

import com.pgalaxyp.fragmento.feature.bard.common.network.packet.BardBasicAbilityPacket;
import com.pgalaxyp.fragmento.feature.bard.common.weapon.WeaponBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class BardWeaponMouseInputClient {

    private BardWeaponMouseInputClient() {
    }

    private static boolean isHoldingBardWeapon(LocalPlayer player) {
        return player.getMainHandItem().getItem() instanceof WeaponBase;
    }

    @SubscribeEvent
    public static void onMouse(InputEvent.MouseButton.Pre event) {
        if (event.getAction() != 1) {
            return;
        }

        if (event.getButton() != 0) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }

        if (mc.screen != null) {
            return;
        }

        if (!isHoldingBardWeapon(player)) {
            return;
        }

        if (player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())) {
            event.setCanceled(true);
            return;
        }

        player.swing(InteractionHand.MAIN_HAND);
        PacketDistributor.sendToServer(new BardBasicAbilityPacket());
        event.setCanceled(true);
    }
}
