package com.pgalaxyp.fragmento.combat.core.def;

import com.pgalaxyp.fragmento.combat.actor.ActorId;
import com.pgalaxyp.fragmento.combat.core.ids.ClassId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public final class SpawnDefaultsResolvers {
    public static SpawnDefaultsResolver fixed(SpawnDefaults defaults) {
        Objects.requireNonNull(defaults);
        return __ -> Optional.of(defaults);
    }

    public static SpawnDefaultsResolver byClass(Function<ActorId, ClassId> selector, Map<ClassId, SpawnDefaults> byClass, SpawnDefaults fallback) {
        Objects.requireNonNull(selector);
        Objects.requireNonNull(byClass);
        Objects.requireNonNull(fallback);
        return actorId -> {
            ClassId cls = selector.apply(Objects.requireNonNull(actorId));
            SpawnDefaults d = cls == null ? null : byClass.get(cls);
            return Optional.of(d == null ? fallback : d);
        };
    }

    public static Map<ClassId, SpawnDefaults> indexByClass(Iterable<SpawnDefaults> defs) {
        Objects.requireNonNull(defs);
        var out = new LinkedHashMap<ClassId, SpawnDefaults>();
        for (SpawnDefaults d : defs) {
            if (d == null) throw new IllegalArgumentException();
            if (out.putIfAbsent(d.classId(), d) != null) {
                throw new IllegalStateException("Duplicate spawn defaults for classId=" + d.classId().value());
            }
        }
        return Map.copyOf(out);
    }

    private SpawnDefaultsResolvers() {}
}