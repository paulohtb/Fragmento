package com.pgalaxyp.fragmento.combat.core.ids;

import java.util.UUID;

public record ActorId(UUID uuid) implements Comparable<ActorId> {
    public ActorId {
        if (uuid == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int compareTo(ActorId other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        int a = Long.compare(uuid.getMostSignificantBits(), other.uuid.getMostSignificantBits());
        if (a != 0) {
            return a;
        }
        return Long.compare(uuid.getLeastSignificantBits(), other.uuid.getLeastSignificantBits());
    }
}