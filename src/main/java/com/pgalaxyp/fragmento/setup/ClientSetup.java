package com.pgalaxyp.fragmento.setup;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.entity.bard.angels.aeolus_angel.AeolusAngelRenderer;
import com.pgalaxyp.fragmento.entity.bard.angels.apollo_angel.ApolloAngelRenderer;
import com.pgalaxyp.fragmento.entity.bard.angels.hercules_angel.HerculesAngelRenderer;
import com.pgalaxyp.fragmento.entity.bard.angels.hipnos_angel.HipnosAngelRenderer;
import com.pgalaxyp.fragmento.entity.bard.angels.orpheus_angel.OrpheusAngelRenderer;
import com.pgalaxyp.fragmento.entity.bard.projectiles.lute_projectile.LuteProjectileRenderer;
import com.pgalaxyp.fragmento.entity.bard.projectiles.drum_projectile.DrumProjectileRenderer;
import com.pgalaxyp.fragmento.entity.bard.projectiles.flute_projectile.FluteProjectileRenderer;
import com.pgalaxyp.fragmento.entity.bard.projectiles.guitar_projectile.GuitarProjectileRenderer;
import com.pgalaxyp.fragmento.entity.bard.projectiles.lyre_projectile.LyreProjectileRenderer;
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

        event.registerEntityRenderer(EntitiesRegistry.TIMER_INDICATOR.get(), TimerEntityRenderer::new);

        event.registerEntityRenderer(EntitiesRegistry.LYRE_PROJECTILE.get(), LyreProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.LUTE_PROJECTILE.get(), LuteProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.DRUM_PROJECTILE.get(), DrumProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.FLUTE_PROJECTILE.get(), FluteProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.GUITAR_PROJECTILE.get(), GuitarProjectileRenderer::new);

        event.registerEntityRenderer(EntitiesRegistry.APOLLO_ANGEL.get(), ApolloAngelRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.HIPNOS_ANGEL.get(), HipnosAngelRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.HERCULES_ANGEL.get(), HerculesAngelRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.AEOLUS_ANGEL.get(), AeolusAngelRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.ORPHEUS_ANGEL.get(), OrpheusAngelRenderer::new);
    }
}