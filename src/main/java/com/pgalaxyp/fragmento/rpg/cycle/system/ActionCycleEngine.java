package com.pgalaxyp.fragmento.rpg.cycle.system;

import com.pgalaxyp.fragmento.rpg.core.ids.*;
import com.pgalaxyp.fragmento.rpg.cycle.api.*;
import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.action.api.*;
import com.pgalaxyp.fragmento.rpg.cycle.bridge.*;
import java.util.*;

public final class ActionCycleEngine implements ActionCycleService {

    private final ActionCycleCatalog catalog;

    public ActionCycleEngine(ActionCycleCatalog catalog) {
        if (catalog == null) throw new IllegalArgumentException();
        this.catalog = catalog;
    }

    @Override
    public Optional<ActionRequest> translate(FrameContext frame, ActorId actorId, WeaponId weaponId, ComboResult comboResult) {
        if (frame == null || actorId == null || weaponId == null || comboResult == null) throw new IllegalArgumentException();
        if (!(comboResult instanceof ComboResult.Progress p)) return Optional.empty();

        var defOpt = catalog.cycleFor(weaponId);
        if (defOpt.isEmpty()) return Optional.empty();

        var def = defOpt.get();
        if (!def.comboId().equals(p.comboId())) return Optional.empty();

        return Optional.of(p.start() ? new ActionRequest.Start(def.actionId()) : ActionRequest.Tick.INSTANCE);
    }
}