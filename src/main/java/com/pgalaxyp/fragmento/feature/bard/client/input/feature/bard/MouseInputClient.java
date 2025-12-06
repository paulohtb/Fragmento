package com.pgalaxyp.fragmento.feature.bard.client.input.feature.bard;

import com.pgalaxyp.fragmento.feature.bard.common.network.packet.BasicAbilityPacket;
import com.pgalaxyp.fragmento.feature.bard.common.weapon.InstrumentBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class MouseInputClient {

    private MouseInputClient() {
    }

    private static boolean isHoldingBardWeapon(LocalPlayer player) {
        return player.getMainHandItem().getItem() instanceof InstrumentBase;
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

        PacketDistributor.sendToServer(new BasicAbilityPacket());
        event.setCanceled(true);
    }
}
