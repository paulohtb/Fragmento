package com.pgalaxyp.fragmento.combat.transport;

public final class NoopSnapshotPort implements SnapshotPort {
    @Override public void publish(GameSnapshot snapshot) {}
}