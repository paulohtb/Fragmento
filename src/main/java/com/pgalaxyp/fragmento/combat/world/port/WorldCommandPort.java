package com.pgalaxyp.fragmento.combat.world.port;

import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.List;

public interface WorldCommandPort {
    void apply(FrameContext frame, GameState state, List<DomainEvent> events);
}