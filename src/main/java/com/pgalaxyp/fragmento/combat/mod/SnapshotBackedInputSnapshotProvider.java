package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.inputModule.port.*;
import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;
import java.util.Objects;

public final class SnapshotBackedInputSnapshotProvider implements InputSnapshotProvider {
    private final ClientSnapshotReceiver receiver;

    public SnapshotBackedInputSnapshotProvider(ClientSnapshotReceiver receiver) {
        this.receiver = Objects.requireNonNull(receiver);
    }

    @Override public InputSnapshotView current() {
        CombatSnapshot snap = receiver.lastSnapshot();
        return snap == null ? InputSnapshotView.empty() : InputSnapshotView.of(snap.frame().frameId());
    }
}