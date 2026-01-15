package com.pgalaxyp.fragmento.combat.content.binding;

import com.pgalaxyp.fragmento.combat.cycle.model.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public interface ActionCycleCatalog {
    Optional<ActionCycleDef> cycleFor(WeaponId weaponId);
}