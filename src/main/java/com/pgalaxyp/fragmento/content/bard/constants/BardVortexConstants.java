package com.pgalaxyp.fragmento.content.bard.constants;

public final class BardVortexConstants {

    private BardVortexConstants() {
    }

    public static final int MINOR_LIFETIME_TICKS = 30;
    public static final int MEDIUM_LIFETIME_TICKS = 80;

    public static final double MINOR_RADIUS = 2.0;
    public static final double MEDIUM_RADIUS = 2.0;

    public static final double MINOR_PULL_STRENGTH = 0.1;
    public static final double MEDIUM_PULL_STRENGTH = 0.1;

    public static final int MEDIUM_GLOWING_TICKS = 40;

    public static final int MINOR_SCAN_INTERVAL_TICKS = 1;
    public static final int MEDIUM_SCAN_INTERVAL_TICKS = 1;

    public static final int MINOR_MAX_AFFECTED_PER_SCAN = 6;
    public static final int MEDIUM_MAX_AFFECTED_PER_SCAN = 12;

    public static final int MINOR_MAX_ACTIVE_PER_OWNER = 2;
    public static final int MEDIUM_MAX_ACTIVE_PER_OWNER = 1;

    public static final double VORTEX_RADIUS = MINOR_RADIUS;
    public static final double VORTEX_PULL_STRENGTH = MINOR_PULL_STRENGTH;
}
