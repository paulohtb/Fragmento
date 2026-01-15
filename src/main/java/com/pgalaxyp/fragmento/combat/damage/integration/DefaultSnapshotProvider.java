package com.pgalaxyp.fragmento.combat.damage.integration;

import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.damage.snapshot.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;

public final class DefaultSnapshotProvider implements DamageSnapshotProvider {

    @Override
    public DamageSnapshot snapshot(GameState state, ActorId source, ActorId target) {
        return new DamageSnapshot(ResistanceProfile.none());
    }
}