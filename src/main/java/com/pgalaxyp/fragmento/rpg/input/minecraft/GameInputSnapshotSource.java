package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.input.bridge.*;
import com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.client.*;

public final class GameInputSnapshotSource implements InputSnapshotProvider {

    @Override
    public InputSnapshotView current() {
        return ClientRpgRuntime.lastSnapshot()
                .map(InputSnapshotView::of)
                .orElse(InputSnapshotView.empty());
    }
}