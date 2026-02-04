package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.engineModule.api.EngineSnapshot;
import java.util.Objects;

public final class ClientSnapshotReceiver {
    private volatile EngineSnapshot last;
    public void acceptSnapshot(EngineSnapshot snapshot) { last = Objects.requireNonNull(snapshot); }
    public EngineSnapshot lastSnapshot() { return last; }
}