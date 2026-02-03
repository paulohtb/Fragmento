package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.engineModule.port.SnapshotPort;
import com.pgalaxyp.fragmento.combat.engineModule.api.EngineSnapshot;
import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityViewSnapshot;
import java.util.*;

public record LocalSnapshotPort(ClientSnapshotReceiver receiver) implements SnapshotPort {
    public LocalSnapshotPort { Objects.requireNonNull(receiver); }

    @Override public void publish(EngineSnapshot snapshot) {
        Objects.requireNonNull(snapshot);
        AbilityViewSnapshot abilities = snapshot.viewOpt(AbilityViewSnapshot.class).orElseGet(() -> new AbilityViewSnapshot(List.of()));
        receiver.acceptSnapshot(new CombatSnapshot(snapshot.frame(), snapshot.actors(), abilities));
    }
}