package com.pgalaxyp.fragmento.cosmetics.client;

import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.client.screen.CosmeticsScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.bus.api.SubscribeEvent;

@EventBusSubscriber(modid = CosmeticsKeys.MOD_ID, value = Dist.CLIENT)
public final class CosmeticsClientInventoryButtonInit {

    private CosmeticsClientInventoryButtonInit() {
    }

    @SubscribeEvent
    public static void onInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (screen == null) return;

        boolean ok = screen instanceof InventoryScreen;
        if (!ok && screen instanceof CreativeModeInventoryScreen) ok = true;
        if (!ok) return;

        int x = Math.addExact(event.getScreen().width / 2, 104);
        int y = Math.subtractExact(event.getScreen().height / 2, 84);

        event.addListener(
                Button.builder(Component.literal("Cosmetics"), new Button.OnPress() {
                    @Override
                    public void onPress(Button b) {
                        Minecraft mc = Minecraft.getInstance();
                        if (mc == null) return;
                        mc.setScreen(new CosmeticsScreen());
                    }
                }).bounds(x, y, 80, 20).build()
        );
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        CosmeticsClientState.clearAll();
    }
}