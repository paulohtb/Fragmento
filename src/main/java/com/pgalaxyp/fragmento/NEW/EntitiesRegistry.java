package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.NEW.newnew.NewNewLuteProjectile;
import com.pgalaxyp.fragmento.entity.bard.angel.aeolus_angel.AeolusAngel;
import com.pgalaxyp.fragmento.entity.bard.angel.apollo_angel.ApolloAngel;
import com.pgalaxyp.fragmento.entity.bard.angel.hercules_angel.HerculesAngel;
import com.pgalaxyp.fragmento.entity.bard.angel.hipnos_angel.HipnosAngel;
import com.pgalaxyp.fragmento.entity.bard.angel.orpheus_angel.OrpheusAngel;
import com.pgalaxyp.fragmento.entity.bard.projectile.drum_projectile.DrumProjectile;
import com.pgalaxyp.fragmento.entity.bard.projectile.flute_projectile.FluteProjectile;
import com.pgalaxyp.fragmento.entity.bard.projectile.guitar_projectile.GuitarProjectile;
import com.pgalaxyp.fragmento.entity.bard.projectile.lyre_projectile.LyreProjectile;
import com.pgalaxyp.fragmento.entity.timerEntity.TimerEntity;
import com.pgalaxyp.fragmento.entity.bard.projectile.lute_projectile.LuteProjectile;
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

    public static final DeferredHolder<EntityType<?>, EntityType<NewLyreProjectile>> NEW_LYRE_PROJECTILE =
            ENTITIES.register("new_lyre_projectile",
                    () -> EntityType.Builder.<NewLyreProjectile>of(NewLyreProjectile::new, MobCategory.MISC)
                            .sized(0.35f, 0.35f)
                            .clientTrackingRange(64)
                            .build("new_lyre_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<NewDrumProjectile>> NEW_DRUM_PROJECTILE =
            ENTITIES.register("new_drum_projectile",
                    () -> EntityType.Builder.<NewDrumProjectile>of(NewDrumProjectile::new, MobCategory.MISC)
                            .sized(0.35f, 0.35f)
                            .clientTrackingRange(64)
                            .build("new_drum_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<NewFluteProjectile>> NEW_FLUTE_PROJECTILE =
            ENTITIES.register("new_flute_projectile",
                    () -> EntityType.Builder.<NewFluteProjectile>of(NewFluteProjectile::new, MobCategory.MISC)
                            .sized(0.35f, 0.35f)
                            .clientTrackingRange(64)
                            .build("new_flute_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<NewSoundWaveEntity>> NEW_SOUNDWAVE_ENTITY =
            ENTITIES.register("new_soundwave_entity",
                    () -> EntityType.Builder.of(NewSoundWaveEntity::new, MobCategory.MISC)
                            .sized(0.35f, 0.35f)
                            .clientTrackingRange(64)
                            .build("new_soundwave_entity"));
    public static final DeferredHolder<EntityType<?>, EntityType<NewFluteSpecialEntity>> NEW_FLUTE_SPECIAL_ENTITY =
            ENTITIES.register("new_flute_special_entity",
                    () -> EntityType.Builder.of(NewFluteSpecialEntity::new, MobCategory.MISC)
                            .sized(9.1f, 0.1f)
                            .clientTrackingRange(64)
                            .build("new_flute_special_entity"));
    public static final DeferredHolder<EntityType<?>, EntityType<NewNewLuteProjectile>> NEW_NEW_LUTE_PROJECTILE =
            ENTITIES.register("new_new_lute_projectile",
                    () -> EntityType.Builder.<NewNewLuteProjectile>of(NewNewLuteProjectile::new, MobCategory.MISC)
                            .sized(0.35f, 0.35f)
                            .clientTrackingRange(64)
                            .build("new_new_lute_projectile"));


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
