package com.pgalaxyp.fragmento.feature.bard.common.init;

import com.pgalaxyp.fragmento.feature.bard.common.spirit.impl.FluteSpirit;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FluteSpiritTypeRegistry {

    private FluteSpiritTypeRegistry() {
    }

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "fragmento");

    public static final DeferredHolder<EntityType<?>, EntityType<FluteSpirit>> FLUTE_SPIRIT =
            ENTITIES.register(
                    "flute_spirit",
                    () -> EntityType.Builder
                            .of(FluteSpirit::new, MobCategory.MISC)
                            .sized(5.0F / 16.0F, 5.0F / 16.0F)
                            .clientTrackingRange(64)
                            .build("flute_spirit")
            );
}
