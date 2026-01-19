package com.pgalaxyp.fragmento.combat.combo.api;

public interface ComboService {
    ComboResult decide(com.pgalaxyp.fragmento.combat.core.ids.ActorId actorId, ComboId comboId, ComboPattern pattern, ComboInput input);
}
