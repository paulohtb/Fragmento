package com.pgalaxyp.fragmento.combat.inputModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;
import java.util.Optional;

public final class InputSnapshotView {
    private static final InputSnapshotView EMPTY = new InputSnapshotView(null);
    private final CombatSnapshot snapshot;

    public static InputSnapshotView empty() { return EMPTY; }

    public boolean isPresent() { return snapshot != null; }

    private InputSnapshotView(CombatSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public long frameIdOrZero() {
        return snapshot == null ? 0L : snapshot.frame().frameId();
    }

    public static InputSnapshotView of(CombatSnapshot snapshot) {
        if (snapshot == null) throw new IllegalArgumentException();
        return new InputSnapshotView(snapshot);
    }

    public Optional<ActorState> findActor(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        return snapshot == null ? Optional.empty() : snapshot.actors().findActor(actorId);
    }
}