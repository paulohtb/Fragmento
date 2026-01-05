package com.pgalaxyp.fragmento.rpg.gameplay.effects;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class EffectRepository {
    private final Map<Long, Effect> effects = new HashMap<>();

    public void put(Effect e) {
        effects.put(e.id().value(), e);
    }

    public Optional<Effect> find(long id) {
        return Optional.ofNullable(effects.get(id));
    }

    public Map<Long, Effect> snapshot() {
        return Map.copyOf(effects);
    }

    public Effect remove(long id) {
        return effects.remove(id);
    }
}