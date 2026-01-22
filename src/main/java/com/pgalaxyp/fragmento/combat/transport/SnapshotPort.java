package com.pgalaxyp.fragmento.combat.transport;

public interface SnapshotPort {
    void publish(GameSnapshot snapshot);
}
