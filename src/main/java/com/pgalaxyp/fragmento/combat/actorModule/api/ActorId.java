package com.pgalaxyp.fragmento.combat.actorModule.api;

import java.util.*;

public record ActorId(UUID value) implements Comparable<ActorId> {
    public ActorId { Objects.requireNonNull(value); }
    @Override public int compareTo(ActorId id) { return value.compareTo(Objects.requireNonNull(id).value); }
}