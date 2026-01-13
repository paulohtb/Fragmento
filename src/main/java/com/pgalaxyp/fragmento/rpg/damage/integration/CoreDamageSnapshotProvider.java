package com.pgalaxyp.fragmento.rpg.damage.integration;

import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.damage.snapshot.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public final class CoreDamageSnapshotProvider implements DamageSnapshotProvider {

    @Override
    public DamageSnapshot snapshot(GameState state, ActorId source, ActorId target) {
        return new DamageSnapshot(ResistanceProfile.none());
    }
}