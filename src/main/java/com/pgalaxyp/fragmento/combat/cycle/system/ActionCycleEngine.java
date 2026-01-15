package com.pgalaxyp.fragmento.combat.cycle.system;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.cycle.api.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.cycle.model.*;
import com.pgalaxyp.fragmento.combat.content.bindings.*;
import java.util.*;

public final class ActionCycleEngine implements ActionCycleService {
    private final ActionCycleCatalog catalog;

    public ActionCycleEngine(ActionCycleCatalog catalog) {
        this.catalog = Objects.requireNonNull(catalog);
    }

    @Override
    public Optional<ActionRequest> translate(ComboResult comboResult, ActorId actorId, WeaponId weaponId, long frameId) {
        if (!(comboResult instanceof ComboResult.Progress p)) return Optional.empty();

        ActionCycleDef def = catalog.cycleFor(weaponId).orElse(null);
        if (def == null || !def.comboId().equals(p.comboId())) return Optional.empty();

        return Optional.of(p.start() ? new ActionRequest.Start(def.actionId()) : ActionRequest.Tick.INSTANCE);
    }
}