package com.pgalaxyp.fragmento.rpg.gameplay.input;

import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.Updatable;
import java.util.Objects;

public final class InputRouter implements Updatable {

    private final CombatInputBinding binding;

    public InputRouter(CombatInputBinding binding) {
        this.binding = Objects.requireNonNull(binding);
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
    }
}