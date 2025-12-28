package com.pgalaxyp.fragmento.tiers.client.ui;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.api.TierStatus;
import com.pgalaxyp.fragmento.tiers.client.TierClientState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT)
public final class TierHudOverlay {

    private static volatile long LAST_VERSION = Long.MIN_VALUE;
    private static volatile Component LAST_TEXT = Component.literal("");

    private TierHudOverlay() {
    }

    @SubscribeEvent
    public static void onRender(final RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        if (mc.player == null) return;
        if (mc.options == null) return;
        if (mc.options.hideGui) return;

        long v = TierClientState.version();
        if (v != LAST_VERSION) {
            LAST_VERSION = v;
            Tier t = TierClientState.get();
            LAST_TEXT = buildLabel(t);
        }

        GuiGraphics gg = event.getGuiGraphics();
        Font font = mc.font;
        if (gg == null || font == null) return;

        gg.drawString(font, LAST_TEXT, 6, 6, 16777215, true);
    }

    private static Component buildLabel(Tier t) {
        if (t == null) return Component.literal("Tier: ?");
        TierStatus st = t.status();
        if (st == TierStatus.ACTIVE) {
            return Component.literal("Tier: " + t.level().value());
        }
        if (st == TierStatus.INACTIVE) {
            return Component.literal("Tier: 0");
        }
        if (st == TierStatus.ERROR) {
            return Component.literal("Tier: error");
        }
        return Component.literal("Tier: ?");
    }
}