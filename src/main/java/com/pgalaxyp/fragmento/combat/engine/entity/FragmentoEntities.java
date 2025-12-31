package com.pgalaxyp.fragmento.combat.engine.entity;

import com.pgalaxyp.fragmento.combat.content.entity.flute.FluteBasicHitEntity;
import com.pgalaxyp.fragmento.combat.content.entity.flute.FluteCastingEntity;
import com.pgalaxyp.fragmento.combat.content.entity.flute.FluteVortexEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class FragmentoEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "fragmento");

    public static final Supplier<EntityType<FluteBasicHitEntity>> FLUTE_BASIC_HIT =
            ENTITIES.register(
                    "flute_basic_hit",
                    () -> EntityType.Builder.<FluteBasicHitEntity>of(
                                    FluteBasicHitEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("fragmento:flute_basic_hit")
            );

    public static final Supplier<EntityType<FluteVortexEntity>> FLUTE_VORTEX =
            ENTITIES.register(
                    "flute_vortex",
                    () -> EntityType.Builder.<FluteVortexEntity>of(
                                    FluteVortexEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("fragmento:flute_vortex")
            );

    public static final Supplier<EntityType<FluteCastingEntity>> FLUTE_CASTING =
            ENTITIES.register(
                    "flute_casting",
                    () -> EntityType.Builder.<FluteCastingEntity>of(
                                    FluteCastingEntity::new,
                                    MobCategory.MISC
                            )
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("fragmento:flute_casting")
            );

    private FragmentoEntities() {}

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
    }
}