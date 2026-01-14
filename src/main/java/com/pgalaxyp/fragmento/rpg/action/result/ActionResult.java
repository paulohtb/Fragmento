package com.pgalaxyp.fragmento.rpg.action.result;

import com.pgalaxyp.fragmento.rpg.action.emit.*;
import java.util.*;

public record ActionResult(List<ActionEmission> emissions, boolean finished, ActionStatus status) {

    public ActionResult {
        Objects.requireNonNull(emissions, "emissions cannot be null");
        Objects.requireNonNull(status, "status cannot be null");

        emissions = List.copyOf(emissions);

        if (status != ActionStatus.ACCEPTED) {
            if (finished) {
                throw new IllegalArgumentException("finished must be false when status is not accepted");
            }
            if (!emissions.isEmpty()) {
                throw new IllegalArgumentException("emissions must be empty when status is not accepted");
            }
        }
    }

    public static ActionResult ignored() {
        return new ActionResult(List.of(), false, ActionStatus.IGNORED);
    }

    public static ActionResult rejected() {
        return new ActionResult(List.of(), false, ActionStatus.REJECTED);
    }

    public static ActionResult accepted(List<ActionEmission> emissions, boolean finished) {
        return new ActionResult(emissions, finished, ActionStatus.ACCEPTED);
    }

    public static ActionResult acceptedNone() {
        return new ActionResult(List.of(), false, ActionStatus.ACCEPTED);
    }

    public static ActionResult finished(List<ActionEmission> emissions) {
        return new ActionResult(emissions, true, ActionStatus.ACCEPTED);
    }
}