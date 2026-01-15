package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client;

import com.mojang.blaze3d.vertex.*;
import com.pgalaxyp.fragmento.combat.ports.dto.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.clientfx.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.*;
import java.util.*;
import net.minecraft.client.*;
import net.neoforged.bus.api.*;
import net.minecraft.client.gui.*;
import net.neoforged.fml.common.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.fml.common.EventBusSubscriber.*;

@EventBusSubscriber(modid = FragmentoMod.MOD_ID, bus = Bus.GAME, value = Dist.CLIENT)
public final class ClientGameEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) { ModVfx.clientTick(); }

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        PoseStack ps = event.getPoseStack();
        ModVfx.render(ps, event.getPartialTick().getGameTimeDeltaPartialTick(true));
    }

    @SubscribeEvent
    public static void onHud(RenderGuiEvent.Post event) {
        Optional<GameSnapshot> snapOpt = ClientModRuntime.lastSnapshot();
        Optional<ActorId> idOpt = ClientModRuntime.localActorId();
        if (snapOpt.isEmpty() || idOpt.isEmpty()) {
            return;
        }

        var s = snapOpt.get().actors().get(idOpt.get());
        if (s == null) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;
        String text = "RPG " + s.healthHearts() + "/" + s.maxHealthHearts();
        GuiGraphics gg = event.getGuiGraphics();
        gg.drawString(font, text, 8, 8, 0xFFFFFF, false);
    }

    private ClientGameEvents() {}
}