package com.pgalaxyp.fragmento.rpg.host.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.engine.input.Intent;
import com.pgalaxyp.fragmento.rpg.engine.lifecycle.EngineLoop;
import net.minecraft.server.level.ServerLevel;

public final class ServerCombatLoop {

    private final EngineLoop engine;

    public ServerCombatLoop(EngineLoop engine) {
        this.engine = engine;
    }

    public void submit(Intent intent, ServerLevel level) {
        engine.submit(intent);
        engine.tick();
    }
}