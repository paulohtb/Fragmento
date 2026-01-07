package com.pgalaxyp.fragmento.rpg.host.minecraft.effect;

import com.pgalaxyp.fragmento.rpg.core.domain.event.EffectTriggered;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface EffectExecutor {
    void execute(EffectTriggered event, ServerLevel level);
}