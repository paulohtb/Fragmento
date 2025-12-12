package com.pgalaxyp.fragmento.features.bard_class.registry.entity;

import com.pgalaxyp.fragmento.features.bard_class.spirit.type.MediumWindVortex;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.MelodyZone;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.MinorWindVortex;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class VortexHelperRegistry {

    private VortexHelperRegistry() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "fragmento");

    public static final DeferredHolder<EntityType<?>, EntityType<MinorWindVortex>> WIND_VORTEX =
            ENTITIES.register(
                    "wind_vortex",
                    () -> EntityType.Builder
                            .of(MinorWindVortex::new, MobCategory.MISC)
                            .sized(4F, 4F)
                            .clientTrackingRange(64)
                            .build("wind_vortex")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<MediumWindVortex>> MEDIUM_WIND_VORTEX =
            ENTITIES.register(
                    "medium_wind_vortex",
                    () -> EntityType.Builder
                            .of(MediumWindVortex::new, MobCategory.MISC)
                            .sized(4F, 4F)
                            .clientTrackingRange(64)
                            .build("medium_wind_vortex")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<MelodyZone>> MELODY_ZONE =
            ENTITIES.register(
                    "melody_zone",
                    () -> EntityType.Builder
                            .of(MelodyZone::new, MobCategory.MISC)
                            .sized(3F, 0.5F)
                            .clientTrackingRange(64)
                            .build("melody_zone")
            );
}
