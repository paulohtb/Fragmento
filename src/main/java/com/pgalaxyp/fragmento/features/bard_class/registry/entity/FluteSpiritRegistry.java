package com.pgalaxyp.fragmento.features.bard_class.registry.entity;

import com.pgalaxyp.fragmento.features.bard_class.spirit.type.FluteSpirit;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FluteSpiritRegistry {

    private FluteSpiritRegistry() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "fragmento");

    public static final DeferredHolder<EntityType<?>, EntityType<FluteSpirit>> FLUTE_SPIRIT =
            ENTITIES.register(
                    "flute_spirit",
                    () -> EntityType.Builder
                            .of(FluteSpirit::new, MobCategory.MISC)
                            .sized(0.45F, 0.45F)
                            .clientTrackingRange(64)
                            .build("flute_spirit")
            );
}
