package com.pgalaxyp.fragmento.combat.core.def;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record WeaponDef(WeaponId id) {
    public WeaponDef { Objects.requireNonNull(id); }
}