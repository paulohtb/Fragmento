package com.pgalaxyp.fragmento.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.*;
import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.effect.*;
import net.neoforged.bus.api.IEventBus;

public class EffectsRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Fragmento.MODID);

    public static void register(IEventBus eventBus) { EFFECTS.register(eventBus); }

    public static final DeferredHolder<MobEffect, PushEffect> PUSH =
            EFFECTS.register("push", PushEffect::new);
    public static final DeferredHolder<MobEffect, SleepEffect> SLEEP =
            EFFECTS.register("sleep", SleepEffect::new);
    public static final DeferredHolder<MobEffect, CharmEffect> CHARMED =
            EFFECTS.register("charmed", CharmEffect::new);
    public static final DeferredHolder<MobEffect, CleanseEffect> CLEANSE =
            EFFECTS.register("cleanse", CleanseEffect::new);
    public static final DeferredHolder<MobEffect, InsomniaEffect> INSOMNIA =
            EFFECTS.register("insomnia", InsomniaEffect::new);
    public static final DeferredHolder<MobEffect, UpstreamEffect> UPSTREAM =
            EFFECTS.register("upstream", UpstreamEffect::new);
    public static final DeferredHolder<MobEffect, NightmareEffect> NIGHTMARE =
            EFFECTS.register("nightmare", NightmareEffect::new);
    public static final DeferredHolder<MobEffect, ConcussionEffect> CONCUSSION =
            EFFECTS.register("concussion", ConcussionEffect::new);
    public static final DeferredHolder<MobEffect, HealingTouchEffect> HEALING_TOUCH =
            EFFECTS.register("healing_touch", HealingTouchEffect::new);
    public static final DeferredHolder<MobEffect, AeolusBlessingEffect> AEOLUS_BLESSING =
            EFFECTS.register("aeolus_blessing", AeolusBlessingEffect::new);
    public static final DeferredHolder<MobEffect, ProjectileRejectionEffect> PROJECTILE_REJECTION =
            EFFECTS.register("projectile_rejection", ProjectileRejectionEffect::new);
}