package com.pgalaxyp.fragmento.combat.random;

import com.pgalaxyp.fragmento.combat.inputModule.port.InputSnapshotProvider;
import com.pgalaxyp.fragmento.combat.inputModule.port.InputSnapshotView;

import java.util.Objects;

public final class ClientInputSnapshotProvider implements InputSnapshotProvider {
    private final ClientSnapshotReceiver receiver;

    public ClientInputSnapshotProvider(ClientSnapshotReceiver receiver) {
        this.receiver = Objects.requireNonNull(receiver);
    }

    @Override
    public InputSnapshotView current() {
        GameSnapshot snap = receiver.lastSnapshot();
        if (snap == null) return InputSnapshotView.empty();
        return InputSnapshotView.of(snap);
    }
}