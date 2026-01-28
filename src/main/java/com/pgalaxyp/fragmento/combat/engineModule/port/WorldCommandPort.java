package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import java.util.List;

public interface WorldCommandPort {
    void apply(FrameContext frame, GameState state, List<FrameEvent> events);
}