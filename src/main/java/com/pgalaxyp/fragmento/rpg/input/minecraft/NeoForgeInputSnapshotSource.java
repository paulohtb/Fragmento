package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.client.ClientRpgRuntime;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputSnapshotProvider;
import com.pgalaxyp.fragmento.rpg.input.bridge.InputSnapshotView;

public final class NeoForgeInputSnapshotSource implements InputSnapshotProvider {

    @Override
    public InputSnapshotView current() {
        var opt = ClientRpgRuntime.lastSnapshot();
        if (opt.isEmpty()) {
            return InputSnapshotView.empty();
        }
        return InputSnapshotView.of(opt.get());
    }
}