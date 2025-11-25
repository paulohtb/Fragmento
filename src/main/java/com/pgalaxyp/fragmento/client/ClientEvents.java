package com.pgalaxyp.fragmento.client;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.client.bardClient.BardWeaponGUI;
import com.pgalaxyp.fragmento.registry.ItensRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import static com.pgalaxyp.fragmento.util.KeyHandler.CHANGE_WEAPON_KEY;

@EventBusSubscriber(modid = Fragmento.MODID, value = Dist.CLIENT)
public class ClientEvents {

    private static boolean shouldSkip(Minecraft mc) {
        return mc.level == null || mc.player == null;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (shouldSkip(mc)) return;

        LocalPlayer player = mc.player;
        processWeaponMenu(mc);
    }

    private static void processWeaponMenu(Minecraft mc) {
        if (!CHANGE_WEAPON_KEY.consumeClick()) return;
        if (mc.screen != null) return;
        if (!BardWeaponGUI.canOpen()) return;

        Item[] instruments = new Item[]{
//                ItensRegistry.GUITAR.get(),
                ItensRegistry.FLUTE.get(),
//                ItensRegistry.LUTE.get(),
//                ItensRegistry.LYRE.get(),
//                ItensRegistry.DRUM.get()
        };

        mc.setScreen(new BardWeaponGUI(instruments, ItensRegistry.FLUTE.get()));
    }

    @SubscribeEvent
    public static void onKeyReleased(ScreenEvent.KeyReleased.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof BardWeaponGUI) {
            mc.setScreen(null);
        }
    }
}