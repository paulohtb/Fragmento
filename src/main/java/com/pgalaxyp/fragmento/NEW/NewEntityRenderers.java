package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.NEW.newnew.SimpleProjectileRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NewEntityRenderers {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntitiesRegistry.NEW_LYRE_PROJECTILE.get(), NewLyreProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.NEW_DRUM_PROJECTILE.get(), NewDrumProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.NEW_FLUTE_PROJECTILE.get(), NewFluteProjectileRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.NEW_SOUNDWAVE_ENTITY.get(), NewSoundWaveRenderer::new);
        event.registerEntityRenderer(EntitiesRegistry.NEW_FLUTE_SPECIAL_ENTITY.get(), NewFluteSpecialRenderer::new);

        event.registerEntityRenderer(EntitiesRegistry.NEW_NEW_LUTE_PROJECTILE.get(), SimpleProjectileRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(NewFluteProjectileModel.LAYER_LOCATION, NewFluteProjectileModel::createBodyLayer);
        event.registerLayerDefinition(NewDrumProjectileModel.LAYER_LOCATION, NewDrumProjectileModel::createBodyLayer);
        event.registerLayerDefinition(NewLyreProjectileModel.LAYER_LOCATION, NewLyreProjectileModel::createBodyLayer);
        event.registerLayerDefinition(NewSoundWaveModel.LAYER_LOCATION, NewSoundWaveModel::createBodyLayer);
        event.registerLayerDefinition(NewFluteSpecialModel.LAYER_LOCATION, NewFluteSpecialModel::createBodyLayer);
    }
}
