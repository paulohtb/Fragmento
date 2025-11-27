package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.registry.EntitiesRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NewEntityRenderers {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntitiesRegistry.NEW_LYRE_PROJECTILE.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.NEW_DRUM_PROJECTILE.get(), NoopRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.NEW_FLUTE_PROJECTILE.get(), NewFluteProjectileRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NewFluteProjectileModel.LAYER_LOCATION, NewFluteProjectileModel::createBodyLayer);
    }
}
