package com.pgalaxyp.fragmento.combat.cycle.api;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public interface ActionCycleService {
    Optional<ActionRequest> translate(FrameContext frame, ActorId actorId, WeaponId weaponId, ComboResult comboResult);
}