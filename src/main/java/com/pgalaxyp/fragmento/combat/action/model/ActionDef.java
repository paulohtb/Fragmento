package com.pgalaxyp.fragmento.combat.action.model;

import java.util.*;

public record ActionDef(ActionId id, ActionPlan plan) {

    public ActionDef {
        Objects.requireNonNull(id);
        Objects.requireNonNull(plan);
    }
}