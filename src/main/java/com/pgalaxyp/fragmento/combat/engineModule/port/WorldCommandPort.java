package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.util.List;

public interface WorldCommandPort {
    void apply(FrameContext frame, GameState state, List<Object> events);
}
