package com.pgalaxyp.fragmento.registry;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.entity.drum_projectile.DrumProjectile;
import com.pgalaxyp.fragmento.entity.flute_projectile.FluteProjectile;
import com.pgalaxyp.fragmento.entity.guitar_projectile.GuitarProjectile;
import com.pgalaxyp.fragmento.entity.lira_projectile.LiraProjectile;
import com.pgalaxyp.fragmento.entity.timer_entity.TimerEntity;
import com.pgalaxyp.fragmento.entity.luteProjectile.LuteProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntitiesRegistry {

    private static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, Fragmento.MODID);

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }

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
    public static final DeferredHolder<EntityType<?>, EntityType<LiraProjectile>> LIRA_PROJECTILE =
            ENTITIES.register("lira_projectile",
                    () -> EntityType.Builder.<LiraProjectile>of(LiraProjectile::new, MobCategory.MISC)
                            .sized(.5f, .5f)
                            .clientTrackingRange(64)
                            .build("lira_projectile"));
    public static final DeferredHolder<EntityType<?>, EntityType<TimerEntity>> TIMER_INDICATOR =
            ENTITIES.register("timer_indicator",
                    () -> EntityType.Builder.<TimerEntity>of(TimerEntity::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(64)
                            .build("timer_indicator"));
}