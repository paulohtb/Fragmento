package com.pgalaxyp.fragmento.rpg.platform.minecraft;

import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.port.SnapshotPort;

public final class MinecraftSnapshotPortStub implements SnapshotPort {

    @Override
    public void publish(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        throw new UnsupportedOperationException();
    }
}