package com.pgalaxyp.fragmento.rpg.core.domain;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;

public record WeaponDef(
        WeaponId id,
        ActionDef action
) {}