package com.pgalaxyp.fragmento.combat.engine.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class FragmentoEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "fragmento");

    public static final Supplier<EntityType<FluteSpiritEntity>> FLUTE_SPIRIT =
            ENTITIES.register(
                    "flute_spirit",
                    () -> EntityType.Builder.<FluteSpiritEntity>of(FluteSpiritEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build("fragmento:flute_spirit")
            );

    private FragmentoEntities() {}

    public static void register(IEventBus modBus) {
        ENTITIES.register(modBus);
    }
}