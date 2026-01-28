package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.engineModule.api.GameSnapshot;

public interface SnapshotPort {
    void publish(GameSnapshot snapshot);
}