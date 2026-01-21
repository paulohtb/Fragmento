package com.pgalaxyp.fragmento.combat.content.bard;

import com.pgalaxyp.fragmento.combat.core.def.*;
import com.pgalaxyp.fragmento.combat.core.ids.ActorId;

public final class BardSpawnDefaultsProvider implements SpawnDefaultsProvider {
    private final SpawnDefaults defaults;

    public BardSpawnDefaultsProvider(int healthHearts, int maxHealthHearts) {
        this.defaults = SpawnDefaults.of(BardIds.BARD, BardIds.FLUTE, healthHearts, maxHealthHearts);
    }

    @Override
    public SpawnDefaults defaultsFor(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        return defaults;
    }
}