package com.pgalaxyp.fragmento.rpg.input.bridge;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import java.util.Optional;

public final class SnapshotView {

    private static final SnapshotView EMPTY = new SnapshotView(null);

    private final GameSnapshot snapshot;

    private SnapshotView(GameSnapshot snapshot) {
        this.snapshot = snapshot;
    }

    public static SnapshotView empty() {
        return EMPTY;
    }

    public static SnapshotView of(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        return new SnapshotView(snapshot);
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