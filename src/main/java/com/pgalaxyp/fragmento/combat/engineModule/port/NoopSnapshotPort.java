package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.engineModule.api.GameSnapshot;

public final class NoopSnapshotPort implements SnapshotPort {
    @Override public void publish(GameSnapshot snapshot) {}
}