package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.engineModule.api.GameSnapshot;
import java.util.Objects;

public final class ClientSnapshotReceiver {
    private volatile GameSnapshot last;

    public void acceptSnapshot(GameSnapshot snapshot) {
        this.last = Objects.requireNonNull(snapshot);
    }

    public GameSnapshot lastSnapshot() {
        return last;
    }
}