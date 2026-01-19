package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.transport.snapshot.api.*;

public interface SnapshotPort {
    void publish(GameSnapshot snapshot);
}
