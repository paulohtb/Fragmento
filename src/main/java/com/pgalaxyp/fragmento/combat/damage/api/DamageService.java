package com.pgalaxyp.fragmento.combat.damage.api;

import com.pgalaxyp.fragmento.combat.damage.domain.*;
import com.pgalaxyp.fragmento.combat.damage.snapshot.*;

public interface DamageService {
    DamageResult resolve(DamageRequest request, DamageSnapshot snapshot);
}