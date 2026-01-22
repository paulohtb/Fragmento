package com.pgalaxyp.fragmento.combat.ability.system;

import com.pgalaxyp.fragmento.combat.ability.api.AbilityFrameView;
import com.pgalaxyp.fragmento.combat.ability.port.AbilityCombatPort;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.flow.*;
import java.util.*;

public final class AbilityView implements FrameSystem {
    private final AbilityCombatPort abilities;

    public AbilityView(AbilityCombatPort abilities) {
        this.abilities = Objects.requireNonNull(abilities);
    }

    @Override
    public void tick(FrameContext frame, GameState state, FrameBus bus) {
        Collection<ActorId> actorIds = state.actors().navigableKeySet();
        bus.view(AbilityFrameView.class, abilities.view(actorIds, frame.frameId()));
    }
}