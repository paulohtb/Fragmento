package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.engineModule.api.EngineSnapshot;

public interface SnapshotPort {
    void publish(EngineSnapshot snapshot);
}