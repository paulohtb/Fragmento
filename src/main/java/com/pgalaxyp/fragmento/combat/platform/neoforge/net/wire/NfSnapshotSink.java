package com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.*;

public final class NfSnapshotSink implements SnapshotPort {
    @Override
    public void publish(GameSnapshot snapshot) {
        if (snapshot == null) throw new IllegalArgumentException();
        NfWire.sendSnapshotToAll(NfWire.encodeSnapshot(snapshot));
    }
}
