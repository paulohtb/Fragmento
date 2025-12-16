package com.pgalaxyp.fragmento.content.bard.registry;

import com.pgalaxyp.fragmento.content.bard.entity.MediumWindVortex;
import com.pgalaxyp.fragmento.content.bard.entity.MinorWindVortex;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class VortexHelperRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "fragmento");

    public static final DeferredHolder<EntityType<?>, EntityType<MinorWindVortex>> WIND_VORTEX =
            ENTITIES.register(
                    "minor_wind_vortex",
                    () -> EntityType.Builder
                            .of(MinorWindVortex::new, MobCategory.MISC)
                            .sized(1.5f, 1.5f)
                            .clientTrackingRange(48)
                            .updateInterval(3)
                            .build("fragmento:minor_wind_vortex")
            );

    public static final DeferredHolder<EntityType<?>, EntityType<MediumWindVortex>> MEDIUM_WIND_VORTEX =
            ENTITIES.register(
                    "medium_wind_vortex",
                    () -> EntityType.Builder
                            .of(MediumWindVortex::new, MobCategory.MISC)
                            .sized(2.5f, 2.5f)
                            .clientTrackingRange(64)
                            .updateInterval(3)
                            .build("fragmento:medium_wind_vortex")
            );

    private VortexHelperRegistry() {
    }
}
