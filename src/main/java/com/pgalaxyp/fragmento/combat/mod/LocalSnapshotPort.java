package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.engineModule.port.SnapshotPort;
import com.pgalaxyp.fragmento.combat.engineModule.api.EngineSnapshot;
import java.util.Objects;

public record LocalSnapshotPort(ClientSnapshotReceiver receiver) implements SnapshotPort {
    public LocalSnapshotPort { Objects.requireNonNull(receiver); }
    @Override public void publish(EngineSnapshot snapshot) { receiver.acceptSnapshot(Objects.requireNonNull(snapshot)); }
}