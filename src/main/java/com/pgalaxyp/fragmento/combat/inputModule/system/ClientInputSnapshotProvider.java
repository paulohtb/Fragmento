package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.inputModule.port.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameSnapshot;
import java.util.Objects;

public final class ClientInputSnapshotProvider implements InputSnapshotProvider {
    private final ClientSnapshotReceiver receiver;

    public ClientInputSnapshotProvider(ClientSnapshotReceiver receiver) {
        this.receiver = Objects.requireNonNull(receiver);
    }

    @Override
    public InputSnapshotView current() {
        GameSnapshot snap = receiver.lastSnapshot();
        return snap == null ? InputSnapshotView.empty() : InputSnapshotView.of(snap);
    }
}