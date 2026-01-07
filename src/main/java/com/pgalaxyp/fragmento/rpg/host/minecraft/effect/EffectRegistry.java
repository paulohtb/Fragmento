package com.pgalaxyp.fragmento.rpg.host.minecraft.effect;

import com.pgalaxyp.fragmento.rpg.core.domain.effect.EffectId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class EffectRegistry {

    private final Map<String, EffectExecutor> executors = new HashMap<>();

    public void register(EffectId id, EffectExecutor executor) {
        executors.put(id.value(), executor);
    }

    public Optional<EffectExecutor> resolve(EffectId id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(executors.get(id.value()));
    }
}