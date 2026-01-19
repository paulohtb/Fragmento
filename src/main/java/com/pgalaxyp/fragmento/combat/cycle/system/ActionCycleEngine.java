package com.pgalaxyp.fragmento.combat.cycle.system;

import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.cycle.api.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import java.util.*;

public final class ActionCycleEngine implements ActionCycleService {
    private final ActionCycleCatalog catalog;

    public ActionCycleEngine(ActionCycleCatalog catalog) { this.catalog = Objects.requireNonNull(catalog); }

    @Override
    public Optional<ActionRequest> translate(ComboResult comboResult, ActorId actorId, WeaponId weaponId, long frameId) {
        if (!(comboResult instanceof ComboResult.Progress p)) return Optional.empty();
        var def = catalog.cycle(p.comboId(), weaponId).orElse(null);
        if (def == null) return Optional.empty();
        return p.end() ? Optional.of(new ActionRequest.Start(def.actionId())) : Optional.empty();
    }
}