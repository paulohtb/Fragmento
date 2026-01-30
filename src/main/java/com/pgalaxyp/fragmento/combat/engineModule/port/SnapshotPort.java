package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;

public interface SnapshotPort {
    void publish(CombatSnapshot snapshot);
}