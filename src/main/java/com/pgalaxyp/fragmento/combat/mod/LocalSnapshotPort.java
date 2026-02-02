package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.engineModule.port.SnapshotPort;
import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;
import java.util.Objects;

public record LocalSnapshotPort(ClientSnapshotReceiver receiver) implements SnapshotPort {
    public LocalSnapshotPort { Objects.requireNonNull(receiver); }

    @Override public void publish(CombatSnapshot snapshot) {
        receiver.acceptSnapshot(Objects.requireNonNull(snapshot));
    }
}