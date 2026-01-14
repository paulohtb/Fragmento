package com.pgalaxyp.fragmento.rpg.action.executor;

import java.util.*;

public record ActionRunId(UUID value) {

    public ActionRunId {
        Objects.requireNonNull(value, "run id cannot be null");
    }

    public static ActionRunId create() {
        return new ActionRunId(UUID.randomUUID());
    }
}