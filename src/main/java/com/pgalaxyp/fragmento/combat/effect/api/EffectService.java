package com.pgalaxyp.fragmento.combat.effect.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;

public interface EffectService {

    EffectOutcome apply(FrameContext frame, GameState state, EffectIntent intent, ActorId source);

    EffectOutcome applyAll(FrameContext frame, GameState state, Iterable<EffectIntent> intents, ActorId source);
}