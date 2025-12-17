package com.pgalaxyp.fragmento.system.entity.behavior;

import com.pgalaxyp.fragmento.system.skill.SkillMode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class SpiritBehaviorRegistry {

    private static final Map<String, Function<SkillMode, SpiritBehavior>> FACTORIES = new HashMap<>();

    private SpiritBehaviorRegistry() {
    }

    public static void register(String id, Function<SkillMode, SpiritBehavior> factory) {
        if (id == null || id.isBlank()) return;
        if (factory == null) return;
        FACTORIES.put(id, factory);
    }

    public static SpiritBehavior create(String id, SkillMode mode) {
        if (id == null) return null;
        Function<SkillMode, SpiritBehavior> f = FACTORIES.get(id);
        if (f == null) return null;
        return f.apply(mode);
    }
}