package com.pgalaxyp.fragmento.cosmetic.client.ui.hook;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetic.client.ui.screen.CosmeticSlotScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT)
public final class CosmeticInventoryTabHook {

    private CosmeticInventoryTabHook() {}

    @SubscribeEvent
    public static void onInit(ScreenEvent.Init.Post event) {
        Screen screen = event.getScreen();
        if (!(screen instanceof InventoryScreen)) {
            return;
        }

        int w = screen.width;
        int h = screen.height;

        int left = subInt(w, 176);
        left = left / 2;

        int top = subInt(h, 166);
        top = top / 2;

        int x = left + 2;
        int y = top + 2;

        Button b = Button.builder(Component.literal("C"), new OpenCosmeticsPress(screen))
                .bounds(x, y, 20, 20)
                .build();

        event.addListener(b);
    }

    private static int subInt(int a, int b) {
        return Math.addExact(a, Math.negateExact(b));
    }

    private record OpenCosmeticsPress(Screen parent) implements Button.OnPress {

        @Override
        public void onPress(Button button) {
            Minecraft mc = Minecraft.getInstance();
            if (mc == null) {
                return;
            }
            mc.setScreen(new CosmeticSlotScreen(parent, CosmeticSlot.HEAD));
        }
    }
}