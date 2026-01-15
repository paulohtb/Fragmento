package com.pgalaxyp.fragmento.combat.core.domain.ids;

public record QueryId(long value) implements Comparable<QueryId> {
    public QueryId {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public static QueryId fromFrame(long frameId, int localIndex) {
        if (frameId < 0 || localIndex < 0) {
            throw new IllegalArgumentException();
        }
        long base = Math.multiplyExact(Math.addExact(frameId, 1L), 1_000_000L);
        long v = Math.addExact(base, Math.addExact(localIndex, 1L));
        return new QueryId(v);
    }

    @Override
    public int compareTo(QueryId other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        return Long.compare(value, other.value);
    }
}