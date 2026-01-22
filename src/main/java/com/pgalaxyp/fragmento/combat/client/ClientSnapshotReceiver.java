package com.pgalaxyp.fragmento.combat.client;

import com.pgalaxyp.fragmento.combat.transport.ClientInboundPort;
import com.pgalaxyp.fragmento.combat.transport.GameSnapshot;
import java.util.Objects;

public final class ClientSnapshotReceiver implements ClientInboundPort {
    private volatile GameSnapshot last;

    @Override
    public void acceptSnapshot(GameSnapshot snapshot) {
        this.last = Objects.requireNonNull(snapshot);
    }

    public GameSnapshot lastSnapshot() {
        return last;
    }
}