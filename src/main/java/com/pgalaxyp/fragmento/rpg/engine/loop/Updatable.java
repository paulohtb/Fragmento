package com.pgalaxyp.fragmento.rpg.engine.loop;

public interface Updatable {
    void update(GameTick tick, TickBus bus);
}