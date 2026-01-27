package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.random.ClientInboundPort;
import com.pgalaxyp.fragmento.combat.random.GameSnapshot;

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