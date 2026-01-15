package com.pgalaxyp.fragmento.combat.content.bridge;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import java.util.*;

public final class ContentComboSource implements ComboDefinitionSource {

    private final ComboCatalog catalog;

    public ContentComboSource(ComboCatalog catalog) { this.catalog = Objects.requireNonNull(catalog); }

    @Override
    public Optional<ComboDef> baseFor(WeaponId weaponId) {
        return catalog.baseFor(weaponId).map(e -> new ComboDef(e.comboId(), e.pattern()));
    }
}