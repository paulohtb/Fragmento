package com.pgalaxyp.fragmento.combat.damage.snapshot;

import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;

public interface DamageSnapshotProvider {
    DamageSnapshot snapshot(GameState state, ActorId source, ActorId target);
}