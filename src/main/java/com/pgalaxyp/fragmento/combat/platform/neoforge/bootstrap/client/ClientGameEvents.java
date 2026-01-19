package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client;

import com.mojang.blaze3d.vertex.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.clientfx.*;
import net.neoforged.api.distmarker.*;
import net.neoforged.bus.api.*;
import net.neoforged.fml.common.*;
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

    private ClientGameEvents() {}
}
