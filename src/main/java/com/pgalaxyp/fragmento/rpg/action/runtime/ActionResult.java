package com.pgalaxyp.fragmento.rpg.action.runtime;

import java.util.*;

public record ActionResult(List<Object> emittedEffects, boolean finished) {

    public ActionResult {
        Objects.requireNonNull(emittedEffects);
        emittedEffects = List.copyOf(emittedEffects);
    }

    public static ActionResult none() {
        return new ActionResult(List.of(), false);
    }

    public static ActionResult finished(List<Object> effects) {
        return new ActionResult(effects, true);
    }
}