package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.transport.GameSnapshot;

public interface SnapshotPort {
    void publish(GameSnapshot snapshot);
}
