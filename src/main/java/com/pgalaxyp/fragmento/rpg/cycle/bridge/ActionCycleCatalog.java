package com.pgalaxyp.fragmento.rpg.cycle.bridge;

import com.pgalaxyp.fragmento.rpg.core.ids.*;
import com.pgalaxyp.fragmento.rpg.cycle.model.*;
import java.util.*;

public interface ActionCycleCatalog {
    Optional<ActionCycleDef> cycleFor(WeaponId weaponId);
}