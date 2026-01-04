package com.pgalaxyp.fragmento.rpg.content.entity;

import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class RpgEntityRegistry {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, FragmentoMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<CutEffectEntity>> CUT =
            ENTITY_TYPES.register(
                    "cut",
                    () -> EntityType.Builder.of(CutEffectEntity::new, MobCategory.MISC)
                            .sized(1.0f, 1.0f)
                            .clientTrackingRange(64)
                            .updateInterval(1)
                            .build(ResourceLocation.fromNamespaceAndPath(FragmentoMod.MODID, "cut").toString())
            );

    public static void register(IEventBus modBus) {
        ENTITY_TYPES.register(modBus);
    }

    private RpgEntityRegistry() {}
}