package com.pgalaxyp.fragmento.rpg_old.client.ui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.pgalaxyp.fragmento.rpg_old.client.ClientContext;
import com.pgalaxyp.fragmento.rpg_old.client.ClientNetworkProxy;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.AbilitySnapshot;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.CombatSnapshot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class InfusedHudOverlay {

    private static final ResourceLocation ICON =
            ResourceLocation.fromNamespaceAndPath("fragmento", "textures/gui/infused.png");

    public static void render(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (!ClientContext.inGame(mc)) return;
        if (!ClientContext.catalystActive(mc)) return;

        CombatSnapshot snap = ClientNetworkProxy.snapshot();
        if (snap == null) return;

        AbilitySnapshot abilities = snap.abilities();
        if (abilities == null) return;

        boolean armed = !abilities.infusedArmed().isEmpty();

        int x = mc.getWindow().getGuiScaledWidth() / 2 + 92;
        int y = mc.getWindow().getGuiScaledHeight() - 22;

        RenderSystem.enableBlend();
        g.blit(ICON, x, y, 0, 0, 16, 16, 16, 16);

        if (!armed) {
            g.fill(x, y, x + 16, y + 16, 0x88000000);
        }

        SkillId selected = abilities.infusedArmed().values().stream().findFirst().orElse(null);
        Time now = ClientNetworkProxy.now();

        if (selected != null && now != null) {
            Time cd = abilities.cooldownEndsAt().get(selected);
            if (cd != null && cd.isAfter(now)) {
                long left = Math.max(0L, cd.ticks() - now.ticks());
                int h = (int) Math.min(16L, (left * 16L) / Math.max(1L, 100L));
                if (h > 0) {
                    g.fill(x, y + (16 - h), x + 16, y + 16, 0x88000000);
                }
            }
        }

        RenderSystem.disableBlend();
    }

    private InfusedHudOverlay() {}
}