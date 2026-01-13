package com.pgalaxyp.fragmento.rpg.input.bridge;

import com.pgalaxyp.fragmento.rpg.ports.dto.*;
import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public final class InputSnapshotView {

    private static final InputSnapshotView EMPTY = new InputSnapshotView(null);

    private final GameSnapshot snapshot;

    private InputSnapshotView(GameSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public static InputSnapshotView empty() {
        return EMPTY;
    }

    public static InputSnapshotView of(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        return new InputSnapshotView(snapshot);
    }

    public boolean isPresent() {
        return snapshot != null;
    }

    public Optional<GameSnapshot> raw() {
        return snapshot == null ? Optional.empty() : Optional.of(snapshot);
    }

    public long frameIdOrZero() {
        if (snapshot == null) {
            return 0L;
        }
        return snapshot.frame().frameId();
    }

    public Optional<ActorState> findActor(ActorId actorId) {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
        if (snapshot == null) {
            return Optional.empty();
        }
        ActorState s = snapshot.actors().get(actorId);
        return s == null ? Optional.empty() : Optional.of(s);
    }
}