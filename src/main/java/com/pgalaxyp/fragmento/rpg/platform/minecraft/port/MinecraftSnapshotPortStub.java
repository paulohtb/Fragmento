package com.pgalaxyp.fragmento.rpg.platform.minecraft.port;

import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.port.SnapshotPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MinecraftSnapshotPortStub implements SnapshotPort {

    private final List<GameSnapshot> published = new ArrayList<>();
    private GameSnapshot last;

    @Override
    public void publish(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        published.add(snapshot);
        last = snapshot;
    }

    public Optional<GameSnapshot> lastSnapshot() {
        return Optional.ofNullable(last);
    }

    public List<GameSnapshot> allSnapshots() {
        return List.copyOf(published);
    }
}