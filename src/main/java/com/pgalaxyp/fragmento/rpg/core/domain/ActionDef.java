package com.pgalaxyp.fragmento.rpg.core.domain;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;

public record ActionDef(
        ActionId id,
        ActionKind kind,
        ComboDef combo
) {}