package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.engineModule.api.GameSnapshot;
import com.pgalaxyp.fragmento.combat.inputModule.system.ClientSnapshotReceiver;
import java.util.Objects;

public record LocalSnapshotPort(ClientSnapshotReceiver receiver) implements SnapshotPort {
    public LocalSnapshotPort { Objects.requireNonNull(receiver); }

    @Override public void publish(GameSnapshot snapshot) {
        receiver.acceptSnapshot(Objects.requireNonNull(snapshot));
    }
}