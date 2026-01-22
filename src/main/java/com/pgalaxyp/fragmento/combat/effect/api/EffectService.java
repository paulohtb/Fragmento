package com.pgalaxyp.fragmento.combat.effect.api;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;

public interface EffectService {
    EffectOutcome applyResolved(FrameContext frame, GameState state, EffectId effectId, ActorId source, ActorId target);
}
