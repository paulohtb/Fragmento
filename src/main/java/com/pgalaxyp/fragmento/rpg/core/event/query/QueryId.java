package com.pgalaxyp.fragmento.rpg.core.event.query;

public record QueryId(long value) {
    public QueryId {
        if (value <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public static QueryId fromFrame(long frameId, int localIndex) {
        if (frameId < 0 || localIndex < 0) {
            throw new IllegalArgumentException();
        }
        long v = (frameId + 1L) * 1_000_000L + (long) localIndex + 1L;
        return new QueryId(v);
    }
}