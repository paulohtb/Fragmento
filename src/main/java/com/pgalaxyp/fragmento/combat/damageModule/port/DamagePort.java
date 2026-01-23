package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.random.*;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageOutcome;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;

public interface DamagePort {
    DamageOutcome resolve(DamageRequested request, FrameContext frame, GameState state);
}