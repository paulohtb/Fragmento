package com.pgalaxyp.fragmento.combat.content;

import com.pgalaxyp.fragmento.combat.content.catalog.*;

public record GameContent(
        ActionCatalog actions,
        ComboCatalog combos,
        EffectCatalog effects,
        ActionCycleCatalog cycles,
        WeaponCatalog weapons,
        ClassCatalog classes
) {}