package com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire;

import com.pgalaxyp.fragmento.combat.ports.SnapshotPort;
import com.pgalaxyp.fragmento.combat.ports.dto.GameSnapshot;

public final class NfSnapshotSink implements SnapshotPort {

    @Override
    public void publish(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        byte[] encoded = NfWire.encodeSnapshot(snapshot);
        NfWire.sendSnapshotToAll(encoded);
    }
}