package com.pgalaxyp.fragmento.rpg.ports;

import com.pgalaxyp.fragmento.rpg.core.content.GameContent;
import com.pgalaxyp.fragmento.rpg.core.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import java.util.List;

public interface WorldCommandPort {
    void apply(FrameContext frame, GameState state, GameContent content, List<StateDelta> deltas);
}