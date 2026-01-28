package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageOutcome;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageRequested;

public interface DamagePort {
    DamageOutcome resolve(DamageRequested request, FrameContext frame, GameState state);
}