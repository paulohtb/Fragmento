package com.pgalaxyp.fragmento.rpg.host.minecraft.effect;

import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;
import net.minecraft.server.level.ServerLevel;

@FunctionalInterface
public interface EffectExecutor {
    void execute(EffectState effect, ServerLevel level);
}