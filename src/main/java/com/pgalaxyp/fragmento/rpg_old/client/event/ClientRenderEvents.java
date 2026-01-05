package com.pgalaxyp.fragmento.rpg_old.client.event;

import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.rpg_old.client.render.CutEffectRenderer;
import com.pgalaxyp.fragmento.rpg_old.client.render.InfusedStrikeRenderer;
import com.pgalaxyp.fragmento.rpg_old.client.render.SpeedZoneRenderer;
import com.pgalaxyp.fragmento.rpg_old.content.entity.RpgEntityRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(
        modid = FragmentoMod.MODID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD
)
public final class ClientRenderEvents {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(RpgEntityRegistry.CUT.get(), CutEffectRenderer::new);
        event.registerEntityRenderer(RpgEntityRegistry.INFUSED_STRIKE.get(), InfusedStrikeRenderer::new);
        event.registerEntityRenderer(RpgEntityRegistry.SPEED_ZONE.get(), SpeedZoneRenderer::new);
    }

    private ClientRenderEvents() {}
}