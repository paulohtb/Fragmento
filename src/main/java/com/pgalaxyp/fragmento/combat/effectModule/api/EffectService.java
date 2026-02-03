package com.pgalaxyp.fragmento.combat.effectModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;

public interface EffectService {
    EffectOutcome applyResolved(FrameContext frame, EffectId effectId, ActorId source, ActorId target);
}