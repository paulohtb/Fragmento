package com.pgalaxyp.fragmento.combat.cycle.api;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import java.util.*;

public interface ActionCycleService {
    Optional<ActionRequest> translate(ComboResult comboResult, ActorId actorId, WeaponId weaponId, long frameId);
}