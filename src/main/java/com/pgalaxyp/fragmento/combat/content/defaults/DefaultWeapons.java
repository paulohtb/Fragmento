package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.catalog.*;
import java.util.*;

final class DefaultWeapons {

    static WeaponCatalog create() {
        return new WeaponCatalog(Set.of(DefaultIds.WEAPON_FLUTE));
    }
}