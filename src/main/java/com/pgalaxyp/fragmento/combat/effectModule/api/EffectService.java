package com.pgalaxyp.fragmento.combat.effectModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;

public interface EffectService {
    EffectOutcome applyResolved(FrameContext frame, GameState state, EffectId effectId, ActorId source, ActorId target);
}