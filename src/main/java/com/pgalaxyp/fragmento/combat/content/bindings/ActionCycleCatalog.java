package com.pgalaxyp.fragmento.combat.content.bindings;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.cycle.model.*;
import java.util.*;

public interface ActionCycleCatalog {
    Optional<ActionCycleDef> cycleFor(WeaponId weaponId);
}