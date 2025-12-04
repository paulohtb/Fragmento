package com.pgalaxyp.fragmento.feature.bard.client.input;

import com.pgalaxyp.fragmento.feature.bard.common.network.packet.BardChargedAbilityPacket;
import com.pgalaxyp.fragmento.feature.bard.common.weapon.WeaponBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class BardWeaponKeyInputClient {

    private BardWeaponKeyInputClient() {
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

        if (BardKeyMappings.BARD_WEAPON_CHARGED == null) {
            return;
        }

        while (BardKeyMappings.BARD_WEAPON_CHARGED.consumeClick()) {
            if (!(player.getMainHandItem().getItem() instanceof WeaponBase)) {
                continue;
            }
            if (player.getCooldowns().isOnCooldown(player.getMainHandItem().getItem())) {
                continue;
            }

            player.swing(InteractionHand.MAIN_HAND);
            PacketDistributor.sendToServer(new BardChargedAbilityPacket());
        }
    }
}
