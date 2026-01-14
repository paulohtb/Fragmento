package com.pgalaxyp.fragmento.rpg.combo.api;

import com.pgalaxyp.fragmento.rpg.combo.state.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public interface ComboService {

    ComboResult decide(ActorId actorId, WeaponId weaponId, ComboInput input);
    void reset(ActorId actorId);
    Optional<ComboState> stateOf(ActorId actorId);
}