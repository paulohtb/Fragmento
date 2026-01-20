package com.pgalaxyp.fragmento.combat.systems;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityFrameView;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.flow.*;
import java.util.*;

public final class AbilityViewSystem implements FrameSystem {
    private final AbilityCombatPort abilities;

    public AbilityViewSystem(AbilityCombatPort abilities) {
        this.abilities = Objects.requireNonNull(abilities);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Collection<ActorId> actorIds = state.actors().navigableKeySet();
        AbilityFrameView view = abilities.view(actorIds, frame.frameId());
        bus.view(AbilityFrameView.class, view);
    }
}
