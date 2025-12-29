package com.pgalaxyp.fragmento.cosmetics.client.render.layer;

import com.pgalaxyp.fragmento.cosmetics.client.state.CosmeticsClientRegistries;
import com.pgalaxyp.fragmento.cosmetics.common.registry.CosmeticRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class CosmeticsClientRenderLayersInit {

    private CosmeticsClientRenderLayersInit() {}

    @SubscribeEvent
    public static void onAddLayers(EntityRenderersEvent.AddLayers event) {
        if (event == null) {
            return;
        }

        CosmeticRegistry registry = CosmeticsClientRegistries.registry();

        for (PlayerSkin.Model skin : event.getSkins()) {
            EntityRenderer<? extends AbstractClientPlayer> r = event.getSkin(skin);
            if (r instanceof PlayerRenderer pr) {
                pr.addLayer(new CosmeticsPlayerLayer(pr, registry));
            }
        }
    }
}