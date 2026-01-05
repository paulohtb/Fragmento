package com.pgalaxyp.fragmento.rpg.gameplay.input;

import com.pgalaxyp.fragmento.rpg.core.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.Updatable;
import com.pgalaxyp.fragmento.rpg.platform.api.player.InputView;
import com.pgalaxyp.fragmento.rpg.platform.api.player.PlayerView;

import java.util.Objects;

public final class InputRouter implements Updatable {

    private final InputView input;
    private final PlayerView player;
    private final CombatInputBinding binding;

    public InputRouter(InputView input, PlayerView player, CombatInputBinding binding) {
        this.input = Objects.requireNonNull(input);
        this.player = Objects.requireNonNull(player);
        this.binding = Objects.requireNonNull(binding);
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
        for (var action : binding.actions()) {
            if (input.pressedThisTick(binding.actionId(action))) {
                bus.publish(new InputEvent(player.actorId(), action));
            }
        }
    }
}