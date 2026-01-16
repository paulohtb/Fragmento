package com.pgalaxyp.fragmento.combat.content.defaults;

import com.pgalaxyp.fragmento.combat.action.model.*;
import com.pgalaxyp.fragmento.combat.content.catalog.*;
import java.util.*;

final class DefaultActions {

    static ActionCatalog create() {
        ActionDef def = new ActionDef(
                DefaultIds.ACTION_FLUTE_CAST,
                new InstantActionPlan(new com.pgalaxyp.fragmento.combat.effect.model.EffectIntent(DefaultIds.EFFECT_FLUTE_MAGIC))
        );

        return new ActionCatalog(Map.of(def.id(), def));
    }
}