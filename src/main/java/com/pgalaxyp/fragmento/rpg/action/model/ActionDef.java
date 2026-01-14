package com.pgalaxyp.fragmento.rpg.action.model;

import java.util.*;

public record ActionDef(ActionId id, ActionPlan plan) {

    public ActionDef {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(plan, "plan cannot be null");
    }
}