package com.pgalaxyp.fragmento.rpg.port;

import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;

public interface SnapshotPort {
    void publish(GameSnapshot snapshot);
}