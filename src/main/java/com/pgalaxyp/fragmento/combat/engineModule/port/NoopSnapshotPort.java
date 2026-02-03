package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.engineModule.api.EngineSnapshot;

public final class NoopSnapshotPort implements SnapshotPort {
    @Override public void publish(EngineSnapshot snapshot) {}
}