package com.pgalaxyp.fragmento.cosmetics.client.render;

import com.pgalaxyp.fragmento.cosmetics.client.render.layer.CosmeticsPlayerLayer;
import com.pgalaxyp.fragmento.cosmetics.internal.registry.CosmeticRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.minecraft.client.resources.PlayerSkin;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CosmeticsClientRenderLayersInit {

    private CosmeticsClientRenderLayersInit() {}

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        if (event == null) return;

        CosmeticRegistry registry = CosmeticsClientRegistryAccess.registry();

        for (PlayerSkin.Model skin : event.getSkins()) {
            EntityRenderer<? extends AbstractClientPlayer> r = event.getSkin(skin);
            if (r instanceof PlayerRenderer pr) {
                pr.addLayer(new CosmeticsPlayerLayer(pr, registry));
            }
        }
    }
}