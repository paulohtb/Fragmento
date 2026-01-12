package com.pgalaxyp.fragmento.rpg.ports;

import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;

public interface SnapshotPort {
    void publish(GameSnapshot snapshot);
}