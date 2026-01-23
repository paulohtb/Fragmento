package com.pgalaxyp.fragmento.combat.inputModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.actorModule.api.ActorState;
import com.pgalaxyp.fragmento.combat.random.GameSnapshot;
import java.util.*;

public final class InputSnapshotView {
    private static final InputSnapshotView EMPTY = new InputSnapshotView(null);
    private final GameSnapshot snapshot;

    private InputSnapshotView(GameSnapshot snapshot) { this.snapshot = snapshot; }

    public static InputSnapshotView empty() { return EMPTY; }

    public static InputSnapshotView of(GameSnapshot snapshot) {
        if (snapshot == null) throw new IllegalArgumentException();
        return new InputSnapshotView(snapshot);
    }

    public boolean isPresent() { return snapshot != null; }

    public Optional<GameSnapshot> raw() { return snapshot == null ? Optional.empty() : Optional.of(snapshot); }

    public long frameIdOrZero() { return snapshot == null ? 0L : snapshot.frame().frameId(); }

    public Optional<ActorState> findActor(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        if (snapshot == null) return Optional.empty();
        return snapshot.actors().findActor(actorId);
    }
}