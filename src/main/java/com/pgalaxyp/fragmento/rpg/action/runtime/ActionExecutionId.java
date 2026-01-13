package com.pgalaxyp.fragmento.rpg.action.runtime;

import java.util.*;

public record ActionExecutionId(UUID value) {

    public ActionExecutionId {
        if (value == null) {
            throw new IllegalArgumentException();
        }
    }

    public static ActionExecutionId create() {
        return new ActionExecutionId(UUID.randomUUID());
    }
}