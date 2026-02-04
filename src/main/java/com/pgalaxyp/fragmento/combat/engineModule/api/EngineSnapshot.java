package com.pgalaxyp.fragmento.combat.engineModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.util.*;

public record EngineSnapshot(FrameContext frame, ActorView actors, Map<Class<?>, Object> views) {
    public EngineSnapshot {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
        var m = new HashMap<>(Objects.requireNonNull(views));
        m.remove(ActorView.class);
        views = m.isEmpty() ? Map.of() : Map.copyOf(m);
    }

    public <T> Optional<T> viewOpt(Class<T> type) {
        Objects.requireNonNull(type);
        Object v = views.get(type);
        return v == null ? Optional.empty() : Optional.of(type.cast(v));
    }
}