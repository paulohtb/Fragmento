package com.pgalaxyp.fragmento.combat.damageModule.api;

import com.pgalaxyp.fragmento.combat.damageModule.port.*;

public interface DamageService {
    DamageResult resolve(DamageRequest request, DamageSnapshot snapshot);
}