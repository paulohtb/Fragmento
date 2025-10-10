package com.pgalaxyp.fragmento.registry;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.effect.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EffectsRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, Fragmento.MODID);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

    public static final DeferredHolder<MobEffect, CharmEffect> CHARMED =
            EFFECTS.register("charmed", CharmEffect::new);
    public static final DeferredHolder<MobEffect, InsomniaEffect> INSOMNIA =
            EFFECTS.register("insomnia", InsomniaEffect::new);
    public static final DeferredHolder<MobEffect, SleepEffect> SLEEP =
            EFFECTS.register("sleep", SleepEffect::new);
    public static final DeferredHolder<MobEffect, VulnerableEffect> VULNERABLE =
            EFFECTS.register("vulnerable", VulnerableEffect::new);
    public static final DeferredHolder<MobEffect, ExposureEffect> EXPOSURE =
            EFFECTS.register("exposure", ExposureEffect::new);
}