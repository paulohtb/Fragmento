package com.pgalaxyp.fragmento.client;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.item.AbstractBardWeapon;
import com.pgalaxyp.fragmento.network.LeftClickPacket;
import com.pgalaxyp.fragmento.registry.ItensRegistry;
import com.pgalaxyp.fragmento.util.TimerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static com.pgalaxyp.fragmento.util.KeyHandler.CHANGE_WEAPON_KEY;

@EventBusSubscriber(modid = Fragmento.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onLeftClickEvent(InputEvent.InteractionKeyMappingTriggered event) {

        if (!event.isAttack()) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator()) return;

        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof AbstractBardWeapon weapon) {
            if (player.getCooldowns().isOnCooldown(weapon)) {
                event.setSwingHand(false);
                event.setCanceled(true);
                return;
            }
            Minecraft.getInstance().getConnection().send(new LeftClickPacket(InteractionHand.MAIN_HAND));
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {

        if (CHANGE_WEAPON_KEY.consumeClick()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player != null && mc.screen == null && ChangeBardWeaponGUI.canOpen()) {
                Item[] instruments = new Item[]{
                        ItensRegistry.GUITAR.get(),
                        ItensRegistry.FLUTE.get(),
                        ItensRegistry.LUTE.get(),
                        ItensRegistry.LIRA.get(),
                        ItensRegistry.DRUM.get()
                };

                mc.setScreen(new ChangeBardWeaponGUI(instruments, ItensRegistry.LUTE.get()));
            }
        }
    }

    @SubscribeEvent
    public static void onKeyReleased(ScreenEvent.KeyReleased.Post event) {

        if (Minecraft.getInstance().screen instanceof ChangeBardWeaponGUI) {
            Minecraft.getInstance().setScreen(null);
        }
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

        for (ServerLevel sLevel : event.getServer().getAllLevels()) {
            TimerHandler.tickAll(sLevel);
        }
    }
}