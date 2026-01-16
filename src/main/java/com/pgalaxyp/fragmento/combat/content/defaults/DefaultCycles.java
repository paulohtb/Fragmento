package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.content.catalog.*;
import com.pgalaxyp.fragmento.combat.cycle.model.*;
import java.util.*;

final class DefaultCycles {

    static ActionCycleCatalog create() {
        ActionCycleDef def = new ActionCycleDef(
                DefaultIds.COMBO_FLUTE,
                DefaultIds.ACTION_FLUTE_CAST
        );

        return new ActionCycleCatalog(Map.of(
                new ActionCycleCatalog.Key(DefaultIds.COMBO_FLUTE, DefaultIds.WEAPON_FLUTE),
                def
        ));
    }
}