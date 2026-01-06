package com.pgalaxyp.fragmento.rpg.engine.loop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UpdateScheduler {
    private final List<Updatable> systems = new ArrayList<>();

    public UpdateScheduler register(Updatable system) {
        systems.add(Objects.requireNonNull(system));
        return this;
    }

    public void updateAll(GameTick tick, TickBus bus) {
        for (var s : systems) {
            s.update(tick, bus);
        }
    }
}