package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import java.util.List;

public interface WorldCommandPort {
    void apply(FrameContext frame, GameState state, GameContent content, List<StateDelta> deltas);
}