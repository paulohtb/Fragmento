package com.pgalaxyp.fragmento.rpg.core.domain;

import java.util.Map;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;

public record WeaponByAction(
        Map<ActionId, WeaponDef> byAction
) {}