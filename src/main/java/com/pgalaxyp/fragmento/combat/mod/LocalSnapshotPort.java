package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.engineModule.port.SnapshotPort;
import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.inputModule.system.ClientSnapshotReceiver;
import java.util.Objects;

public record LocalSnapshotPort(ClientSnapshotReceiver receiver) implements SnapshotPort {
    public LocalSnapshotPort {
        Objects.requireNonNull(receiver);
    }

    @Override public void publish(CombatSnapshot snapshot) {
        receiver.acceptSnapshot(Objects.requireNonNull(snapshot));
    }
}