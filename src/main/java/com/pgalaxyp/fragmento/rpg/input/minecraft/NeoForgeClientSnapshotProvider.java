package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.client.ClientRpgRuntime;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.SnapshotView;

public final class NeoForgeClientSnapshotProvider implements SnapshotProvider {

    @Override
    public SnapshotView current() {
        var opt = ClientRpgRuntime.lastSnapshot();
        if (opt.isEmpty()) {
            return SnapshotView.empty();
        }
        return SnapshotView.of(opt.get());
    }
}