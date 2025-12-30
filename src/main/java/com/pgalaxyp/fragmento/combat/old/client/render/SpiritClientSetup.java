package com.pgalaxyp.fragmento.combat.old.client.render;

import com.pgalaxyp.fragmento.combat.old.client.render.entity.FluteSpiritRenderer;
import com.pgalaxyp.fragmento.combat.old.content.bard.registry.FluteSkillEntityRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public final class SpiritClientSetup {

    private SpiritClientSetup() {
    }

    public static void init(IEventBus modBus) {
        modBus.addListener(SpiritClientSetup::registerRenderers);
    }

    private static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
                FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
                FluteSpiritRenderer::new
        );
    }
}