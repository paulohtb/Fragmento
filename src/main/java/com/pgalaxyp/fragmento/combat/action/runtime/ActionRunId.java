package com.pgalaxyp.fragmento.combat.action.runtime;

import java.util.*;

public record ActionRunId(UUID value) {
    public ActionRunId { Objects.requireNonNull(value, "run id cannot be null"); }
    public static ActionRunId create() { return new ActionRunId(UUID.randomUUID()); }
}