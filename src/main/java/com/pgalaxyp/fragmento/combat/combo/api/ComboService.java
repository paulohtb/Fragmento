package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.combo.state.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public interface ComboService {

    ComboResult decide(ActorId actorId, WeaponId weaponId, ComboInput input);
    void reset(ActorId actorId);
    Optional<ComboState> stateOf(ActorId actorId);
}