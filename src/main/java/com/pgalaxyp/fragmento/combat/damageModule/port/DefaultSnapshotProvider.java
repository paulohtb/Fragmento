package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import com.pgalaxyp.fragmento.combat.random.GameState;
import java.util.Objects;

public final class DefaultSnapshotProvider implements DamageSnapshotProvider {

    @Override
    public DamageSnapshot snapshot(GameState state, ActorId source, ActorId target) {
        Objects.requireNonNull(state);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);
        return new DamageSnapshot(ResistanceProfile.none());
    }
}