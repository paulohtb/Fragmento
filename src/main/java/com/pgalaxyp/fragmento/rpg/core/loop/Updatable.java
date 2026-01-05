package com.pgalaxyp.fragmento.rpg.core.loop;

public interface Updatable {
    void update(GameTick tick, TickBus bus);
}