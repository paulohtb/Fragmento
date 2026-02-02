package com.pgalaxyp.fragmento.combat.inputModule.port;

public record InputSnapshotView(boolean present, long frameId) {
    private static final InputSnapshotView EMPTY = new InputSnapshotView(false, 0L);

    public InputSnapshotView {
        if (frameId < 0) throw new IllegalArgumentException();
        if (!present && frameId != 0L) throw new IllegalArgumentException();
    }

    public static InputSnapshotView empty() { return EMPTY; }
    public static InputSnapshotView of(long frameId) { return new InputSnapshotView(true, frameId); }
    public boolean isPresent() { return present; }
    public long frameIdOrZero() { return present ? frameId : 0L; }
}