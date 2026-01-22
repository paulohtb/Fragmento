package com.pgalaxyp.fragmento.combat.input.bridge;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.actor.ActorState;
import com.pgalaxyp.fragmento.combat.transport.GameSnapshot;

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
        ActorState s = snapshot.actors().get(actorId);
        return s == null ? Optional.empty() : Optional.of(s);
    }
}