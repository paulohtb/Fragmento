package com.pgalaxyp.fragmento.combat.random;

public final class NoopSnapshotPort implements SnapshotPort {
    @Override public void publish(GameSnapshot snapshot) {}
}