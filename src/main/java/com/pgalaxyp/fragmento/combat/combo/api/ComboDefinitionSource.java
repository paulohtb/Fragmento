package com.pgalaxyp.fragmento.combat.combo.api;

import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public interface ComboDefinitionSource {
    Optional<ComboDef> baseFor(WeaponId weaponId);
}