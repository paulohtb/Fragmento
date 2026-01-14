package com.pgalaxyp.fragmento.rpg.combo.registry;

import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public interface ComboCatalog {
    Optional<ComboDefinition> baseFor(WeaponId weaponId);
}