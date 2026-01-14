package com.pgalaxyp.fragmento.rpg.cycle.api;

import com.pgalaxyp.fragmento.rpg.core.ids.*;
import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.action.api.*;
import java.util.*;

public interface ActionCycleService {
    Optional<ActionRequest> translate(FrameContext frame, ActorId actorId, WeaponId weaponId, ComboResult comboResult);
}