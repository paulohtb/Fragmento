package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import java.util.Objects;

public final class AbilityView implements FrameSystem {
    private final AbilityPort abilities;

    public AbilityView(AbilityPort abilities) {
        this.abilities = Objects.requireNonNull(abilities);
    }

    @Override
    public void tick(FrameContext frame, Object state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);
        GameState gs = (GameState) state;
        var ids = bus.viewOpt(ActorView.class).map(ActorView::ids).orElseGet(() -> gs.actors().ids());
        bus.view(AbilityViewSnapshot.class, abilities.view(ids, frame.frameId()));
    }
}