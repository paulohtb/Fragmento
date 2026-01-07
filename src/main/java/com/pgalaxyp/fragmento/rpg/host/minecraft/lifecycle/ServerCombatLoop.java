package com.pgalaxyp.fragmento.rpg.host.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.engine.input.Intent;
import com.pgalaxyp.fragmento.rpg.engine.lifecycle.EngineLoop;
import com.pgalaxyp.fragmento.rpg.host.minecraft.effect.MinecraftEffectExecutor;
import net.minecraft.server.level.ServerLevel;

public final class ServerCombatLoop {

    private final EngineLoop engine;
    private final MinecraftEffectExecutor effects;

    public ServerCombatLoop(
            EngineLoop engine,
            MinecraftEffectExecutor effects
    ) {
        this.engine = engine;
        this.effects = effects;
    }

    public void submit(Intent intent, ServerLevel level) {
        engine.submit(intent);
        engine.tick().ifPresent(snapshot ->
                snapshot.effects().forEach(effect ->
                        effects.execute(effect, level)
                )
        );
    }
}