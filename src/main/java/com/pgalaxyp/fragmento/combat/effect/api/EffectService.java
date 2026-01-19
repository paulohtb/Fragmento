package com.pgalaxyp.fragmento.combat.effect.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.effect.model.*;

public interface EffectService {
    EffectOutcome applyResolved(FrameContext frame, GameState state, EffectId effectId, com.pgalaxyp.fragmento.combat.core.ids.ActorId source, com.pgalaxyp.fragmento.combat.core.ids.ActorId target);
}
