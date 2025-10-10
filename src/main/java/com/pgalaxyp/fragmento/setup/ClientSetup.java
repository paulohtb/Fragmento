package com.pgalaxyp.fragmento.setup;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.entity.luteProjectile.LuteProjectileRenderer;
import com.pgalaxyp.fragmento.entity.drum_projectile.DrumProjectileRenderer;
import com.pgalaxyp.fragmento.entity.flute_projectile.FluteProjectileRenderer;
import com.pgalaxyp.fragmento.entity.guitar_projectile.GuitarProjectileRenderer;
import com.pgalaxyp.fragmento.entity.lira_projectile.LiraProjectileRenderer;
import com.pgalaxyp.fragmento.entity.timer_entity.TimerEntityRenderer;
import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Fragmento.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void rendererRegister(EntityRenderersEvent.RegisterRenderers event) {

        event.registerEntityRenderer(EntitiesRegistry.LUTE_PROJECTILE.get(), LuteProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.DRUM_PROJECTILE.get(), DrumProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.FLUTE_PROJECTILE.get(), FluteProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.GUITAR_PROJECTILE.get(), GuitarProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.LIRA_PROJECTILE.get(), LiraProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.TIMER_INDICATOR.get(), TimerEntityRenderer::new);
    }
}