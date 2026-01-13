package com.pgalaxyp.fragmento.rpg.damage.snapshot;

import com.pgalaxyp.fragmento.rpg.core.state.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public interface DamageSnapshotProvider {
    DamageSnapshot snapshot(GameState state, ActorId source, ActorId target);
}