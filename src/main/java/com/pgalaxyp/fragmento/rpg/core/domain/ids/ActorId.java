package com.pgalaxyp.fragmento.rpg.core.domain.ids;

public record ActorId(long value) implements Comparable<ActorId> {
    public ActorId {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public int compareTo(ActorId other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        return Long.compare(value, other.value);
    }
}