package com.pgalaxyp.fragmento.registry;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.entity.bard.angels.aeolus_angel.AeolusAngel;
import com.pgalaxyp.fragmento.entity.bard.angels.apollo_angel.ApolloAngel;
import com.pgalaxyp.fragmento.entity.bard.angels.hercules_angel.HerculesAngel;
import com.pgalaxyp.fragmento.entity.bard.angels.hipnos_angel.HipnosAngel;
import com.pgalaxyp.fragmento.entity.bard.angels.orpheus_angel.OrpheusAngel;
import com.pgalaxyp.fragmento.entity.bard.projectiles.drum_projectile.DrumProjectile;
import com.pgalaxyp.fragmento.entity.bard.projectiles.flute_projectile.FluteProjectile;
import com.pgalaxyp.fragmento.entity.bard.projectiles.guitar_projectile.GuitarProjectile;
import com.pgalaxyp.fragmento.entity.bard.projectiles.lyre_projectile.LyreProjectile;
import com.pgalaxyp.fragmento.entity.timer_entity.TimerEntity;
import com.pgalaxyp.fragmento.entity.bard.projectiles.lute_projectile.LuteProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntitiesRegistry {

    private static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Fragmento.MODID);

    public static void register(IEventBus eventBus) { ENTITIES.register(eventBus); }

    public static final DeferredHolder<EntityType<?>, EntityType<TimerEntity>> TIMER_INDICATOR =
            ENTITIES.register("timer_indicator",
                    () -> EntityType.Builder.<TimerEntity>of(TimerEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(64)
                            .build("timer_indicator"));

    public static final DeferredHolder<EntityType<?>, EntityType<LyreProjectile>> LYRE_PROJECTILE =
            ENTITIES.register("lyre_projectile",
                    () -> EntityType.Builder.<LyreProjectile>of(LyreProjectile::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("lyre_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<LuteProjectile>> LUTE_PROJECTILE =
            ENTITIES.register("lute_projectile",
                    () -> EntityType.Builder.<LuteProjectile>of(LuteProjectile::new, MobCategory.MISC)
                    .sized(.5f, .5f)
                    .clientTrackingRange(64)
                    .build("lute_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<DrumProjectile>> DRUM_PROJECTILE =
            ENTITIES.register("drum_projectile",
                    () -> EntityType.Builder.<DrumProjectile>of(DrumProjectile::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("drum_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<FluteProjectile>> FLUTE_PROJECTILE =
            ENTITIES.register("flute_projectile",
                    () -> EntityType.Builder.<FluteProjectile>of(FluteProjectile::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("flute_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<GuitarProjectile>> GUITAR_PROJECTILE =
            ENTITIES.register("guitar_projectile",
                    () -> EntityType.Builder.<GuitarProjectile>of(GuitarProjectile::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("guitar_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<AeolusAngel>> AEOLUS_ANGEL =
            ENTITIES.register("aeolus_angel",
                    () -> EntityType.Builder.<AeolusAngel>of(AeolusAngel::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("aeolus_angel"));
    public static final DeferredHolder<EntityType<?>, EntityType<ApolloAngel>> APOLLO_ANGEL =
            ENTITIES.register("apollo_angel",
                    () -> EntityType.Builder.<ApolloAngel>of(ApolloAngel::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("apollo_angel"));
    public static final DeferredHolder<EntityType<?>, EntityType<HerculesAngel>> HERCULES_ANGEL =
            ENTITIES.register("hercules_angel",
                    () -> EntityType.Builder.<HerculesAngel>of(HerculesAngel::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("hercules_angel"));
    public static final DeferredHolder<EntityType<?>, EntityType<HipnosAngel>> HIPNOS_ANGEL =
            ENTITIES.register("hipnos_angel",
                    () -> EntityType.Builder.<HipnosAngel>of(HipnosAngel::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("hipnos_angel"));
    public static final DeferredHolder<EntityType<?>, EntityType<OrpheusAngel>> ORPHEUS_ANGEL =
            ENTITIES.register("orpheus_angel",
                    () -> EntityType.Builder.<OrpheusAngel>of(OrpheusAngel::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("orpheus_angel"));
}