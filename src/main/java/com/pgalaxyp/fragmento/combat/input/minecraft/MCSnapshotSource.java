package com.pgalaxyp.fragmento.combat.input.minecraft;

import com.pgalaxyp.fragmento.combat.input.bridge.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client.*;

public final class MCSnapshotSource implements InputSnapshotProvider {

    @Override
    public InputSnapshotView current() { return ClientModRuntime.lastSnapshot().map(InputSnapshotView::of).orElse(InputSnapshotView.empty()); }
}