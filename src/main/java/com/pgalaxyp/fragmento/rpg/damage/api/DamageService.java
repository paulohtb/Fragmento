package com.pgalaxyp.fragmento.rpg.damage.api;

import com.pgalaxyp.fragmento.rpg.damage.domain.*;
import com.pgalaxyp.fragmento.rpg.damage.snapshot.*;

public interface DamageService {
    DamageResult resolve(DamageRequest request, DamageSnapshot snapshot);
}