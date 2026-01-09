package com.pgalaxyp.fragmento.rpg.port;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import java.util.List;

public interface WorldCommandPort {
    void apply(FrameContext frame, GameState state, RpgContent content, List<StateDelta> deltas);
}