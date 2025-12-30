package com.pgalaxyp.fragmento.combat.old.content.bard.registry;

import com.pgalaxyp.fragmento.combat.old.system.entity.host.BardSpiritEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class FluteSkillEntityRegistry {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, "fragmento");

    public static final DeferredHolder<EntityType<?>, EntityType<BardSpiritEntity>> FLUTE_SPIRIT =
            ENTITIES.register(
                    "flute_spirit",
                    () -> EntityType.Builder
                            .of(BardSpiritEntity::new, MobCategory.MISC)
                            .sized(0.6f, 0.6f)
                            .clientTrackingRange(96)
                            .updateInterval(1)
                            .build("fragmento:flute_spirit")
            );

    private FluteSkillEntityRegistry() {
    }
}