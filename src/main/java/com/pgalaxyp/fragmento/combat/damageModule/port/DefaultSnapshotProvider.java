package com.pgalaxyp.fragmento.combat.damageModule.port;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import java.util.Objects;

public final class DefaultSnapshotProvider implements DamageSnapshotProvider {
    @Override public DamageSnapshot snapshot(ActorStateView state, ActorId source, ActorId target) {
        Objects.requireNonNull(state);
        Objects.requireNonNull(source);
        Objects.requireNonNull(target);

        return new DamageSnapshot(ResistanceProfile.none());
    }
}