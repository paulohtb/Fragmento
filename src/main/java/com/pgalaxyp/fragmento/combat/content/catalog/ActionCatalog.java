package com.pgalaxyp.fragmento.combat.content.catalog;

import com.pgalaxyp.fragmento.combat.action.model.*;
import java.util.*;

public final class ActionCatalog {

    private final Map<ActionId, ActionDef> defs;

    public ActionCatalog(Map<ActionId, ActionDef> defs) {
        this.defs = Map.copyOf(defs);
    }

    public Optional<ActionDef> action(ActionId id) {
        return Optional.ofNullable(defs.get(id));
    }
}