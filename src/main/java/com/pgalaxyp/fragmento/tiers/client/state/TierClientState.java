package com.pgalaxyp.fragmento.tiers.client.state;

public final class TierClientState {

    private static volatile Entry SELF;

    private TierClientState() {}

    public static int level() {
        Entry e = SELF;
        return e == null ? 0 : e.level;
    }

    public static long version() {
        Entry e = SELF;
        return e == null ? 0L : e.version;
    }

    public static void update(int level, long version) {
        Entry cur = SELF;
        if (cur != null && version < cur.version) {
            return;
        }

        SELF = new Entry(Math.max(0, level), version);
    }

    private record Entry(int level, long version) {}
}