package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.snapshotModule.api.CombatSnapshot;
import java.util.Objects;

public final class ClientSnapshotReceiver {
    private volatile CombatSnapshot last;
    public void acceptSnapshot(CombatSnapshot snapshot) { last = Objects.requireNonNull(snapshot); }

    public CombatSnapshot lastSnapshot() { return last; }
}