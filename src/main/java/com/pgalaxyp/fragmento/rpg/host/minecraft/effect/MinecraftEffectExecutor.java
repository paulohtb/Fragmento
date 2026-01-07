package com.pgalaxyp.fragmento.rpg.host.minecraft.effect;

import com.pgalaxyp.fragmento.rpg.core.domain.event.EffectTriggered;
import net.minecraft.server.level.ServerLevel;

public final class MinecraftEffectExecutor {

    private final EffectRegistry registry;

    public MinecraftEffectExecutor(EffectRegistry registry) {
        this.registry = registry;
    }

    public void execute(EffectTriggered event, ServerLevel level) {
        if (event == null || level == null) return;

        registry.resolve(event.effectId())
                .ifPresent(exec -> exec.execute(event, level));
    }
}