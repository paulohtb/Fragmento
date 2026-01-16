package com.pgalaxyp.fragmento.combat.core.ids;

import java.util.*;

public record ActorId(UUID uuid) implements Comparable<ActorId> {
    public ActorId { uuid = Objects.requireNonNull(uuid); }
    @Override public int compareTo(ActorId o) { return uuid.compareTo(Objects.requireNonNull(o).uuid); }
}