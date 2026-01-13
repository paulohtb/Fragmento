package com.pgalaxyp.fragmento.rpg.action.runtime;

import java.util.*;

public record ActionContext(Object actorId, long frameId) {

    public ActionContext {
        Objects.requireNonNull(actorId);
        if (frameId < 0) {
            throw new IllegalArgumentException();
        }
    }
}