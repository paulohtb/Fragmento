package com.pgalaxyp.fragmento.feature.bard_class.client.render;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;
import net.minecraft.world.entity.EntityType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class SpiritRenderRegistry {

    private SpiritRenderRegistry() {
    }

    private interface Entry {
        void register(EntityRenderersEvent.RegisterRenderers event);
    }

    private static final List<Entry> ENTRIES = new ArrayList<>();

    public static <E extends SpiritBase> void register(
            DeferredHolder<EntityType<?>, EntityType<E>> typeHolder,
            Function<EntityRendererProvider.Context, EntityRenderer<? super E>> factory
    ) {
        ENTRIES.add(event -> event.registerEntityRenderer(typeHolder.get(), factory::apply));
    }

    public static void apply(EntityRenderersEvent.RegisterRenderers event) {
        for (Entry entry : ENTRIES) {
            entry.register(event);
        }
    }
}
