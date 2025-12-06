package com.pgalaxyp.fragmento.feature.bard.client.input.feature.bard;

import com.pgalaxyp.fragmento.feature.bard.common.network.packet.ChargedAbilityPacket;
import com.pgalaxyp.fragmento.feature.bard.common.weapon.InstrumentBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class KeyInputClient {

    private KeyInputClient() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) {
            return;
        }

        if (mc.screen != null) {
            return;
        }

        if (KeyMappings.BARD_WEAPON_CHARGED == null) {
            return;
        }

        while (KeyMappings.BARD_WEAPON_CHARGED.consumeClick()) {
            if (!(player.getMainHandItem().getItem() instanceof InstrumentBase)) {
                continue;
            }
            if (player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())) {
                continue;
            }

            PacketDistributor.sendToServer(new ChargedAbilityPacket());
        }
    }
}
