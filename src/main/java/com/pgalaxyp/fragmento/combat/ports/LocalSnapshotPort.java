package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.transport.GameSnapshot;
import java.util.Objects;

public final class LocalSnapshotPort implements SnapshotPort {
    private final ClientInboundPort inbound;

    public LocalSnapshotPort(ClientInboundPort inbound) {
        this.inbound = Objects.requireNonNull(inbound);
    }

    @Override
    public void publish(GameSnapshot snapshot) {
        inbound.acceptSnapshot(Objects.requireNonNull(snapshot));
    }
}