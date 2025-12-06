package com.pgalaxyp.fragmento.feature.bard_class.common.init;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.impl.FluteVortex;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FluteVortexRegistry {

    private FluteVortexRegistry() {
    }

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "fragmento");

    public static final DeferredHolder<EntityType<?>, EntityType<FluteVortex>> FLUTE_VORTEX =
            ENTITIES.register(
                    "flute_vortex",
                    () -> EntityType.Builder
                            .of(FluteVortex::new, MobCategory.MISC)
                            .sized(0.8F, 0.8F)
                            .clientTrackingRange(64)
                            .build("flute_vortex")
            );
}
