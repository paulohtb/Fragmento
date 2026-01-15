package com.pgalaxyp.fragmento.combat.cycle.system;

import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.cycle.api.*;
import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.action.api.*;
import com.pgalaxyp.fragmento.combat.cycle.model.*;
import com.pgalaxyp.fragmento.combat.content.binding.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public final class ActionCycleEngine implements ActionCycleService {

    private final ActionCycleCatalog catalog;
    public ActionCycleEngine(ActionCycleCatalog catalog) { this.catalog = Objects.requireNonNull(catalog); }

    @Override
    public Optional<ActionRequest> translate(FrameContext frame, ActorId actorId, WeaponId weaponId, ComboResult comboResult) {
        if (!(comboResult instanceof ComboResult.Progress progress)) {
            return Optional.empty();
        }

        ActionCycleDef def = catalog.cycleFor(weaponId).orElse(null);
        if (def == null || !def.comboId().equals(progress.comboId())) {
            return Optional.empty();
        }

        return Optional.of(progress.start() ? new ActionRequest.Start(def.actionId()) : ActionRequest.Tick.INSTANCE);
    }
}