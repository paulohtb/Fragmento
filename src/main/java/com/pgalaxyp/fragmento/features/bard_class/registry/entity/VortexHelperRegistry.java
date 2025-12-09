package com.pgalaxyp.fragmento.features.bard_class.registry.entity;

import com.pgalaxyp.fragmento.features.bard_class.spirit.type.WindVortex;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class VortexHelperRegistry {

    private VortexHelperRegistry() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "fragmento");

    public static final DeferredHolder<EntityType<?>, EntityType<WindVortex>> WIND_VORTEX =
            ENTITIES.register(
                    "wind_vortex",
                    () -> EntityType.Builder
                            .of(WindVortex::new, MobCategory.MISC)
                            .sized(4F, 4F)
                            .clientTrackingRange(64)
                            .build("wind_vortex")
            );
}
