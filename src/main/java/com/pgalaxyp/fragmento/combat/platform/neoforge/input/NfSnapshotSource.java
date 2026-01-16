package com.pgalaxyp.fragmento.combat.platform.neoforge.input;

import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client.*;

public final class NfSnapshotSource implements InputSnapshotProvider {
    @Override
    public InputSnapshotView current() {
        return ClientModRuntime.lastSnapshot().map(InputSnapshotView::of).orElse(InputSnapshotView.empty());
    }
}