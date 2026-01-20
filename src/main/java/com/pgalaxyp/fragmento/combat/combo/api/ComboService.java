package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;

public interface ComboService {
    ComboDecision decide(ActorId actorId, ComboId comboId, ComboPattern pattern, ComboInput input);
}