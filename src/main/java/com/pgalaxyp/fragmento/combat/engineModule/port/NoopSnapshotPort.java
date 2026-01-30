package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;

public final class NoopSnapshotPort implements SnapshotPort {
    @Override public void publish(CombatSnapshot snapshot) {}
}