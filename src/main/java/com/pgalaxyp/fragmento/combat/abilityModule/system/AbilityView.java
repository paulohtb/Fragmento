package com.pgalaxyp.fragmento.combat.abilityModule.system;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.random.*;
import com.pgalaxyp.fragmento.combat.abilityModule.port.AbilityPort;
import java.util.*;

public final class AbilityView implements FrameSystem {
    private final AbilityPort abilities;

    public AbilityView(AbilityPort abilities) { this.abilities = Objects.requireNonNull(abilities); }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(bus);

        var ids = bus.viewOpt(ActorView.class).map(ActorView::ids).orElseGet(() -> state.actors().ids());
        bus.view(AbilityViewSnapshot.class, abilities.view(ids, frame.frameId()));
    }
}