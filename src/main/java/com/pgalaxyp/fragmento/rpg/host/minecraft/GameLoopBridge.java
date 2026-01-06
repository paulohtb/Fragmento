package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.engine.loop.GameLoop;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class GameLoopBridge {

    private final GameLoop loop;

    public GameLoopBridge(GameLoop loop) {
        this.loop = loop;
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        loop.tick();
    }
}