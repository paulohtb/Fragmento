package com.pgalaxyp.fragmento.rpg.host.minecraft.effect;

import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;
import net.minecraft.server.level.ServerLevel;

public final class MinecraftEffectExecutor {

    private final EffectRegistry registry;

    public MinecraftEffectExecutor(EffectRegistry registry) {
        this.registry = registry;
    }

    public void execute(EffectState effect, ServerLevel level) {
        if (effect == null || level == null) return;

        registry.resolve(effect.effect())
                .ifPresent(exec -> exec.execute(effect, level));
    }
}