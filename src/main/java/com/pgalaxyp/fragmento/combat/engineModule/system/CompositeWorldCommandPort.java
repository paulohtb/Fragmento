package com.pgalaxyp.fragmento.combat.engineModule.system;

import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.util.*;

public record CompositeWorldCommandPort(List<WorldCommandPort> ports) implements WorldCommandPort {
    public CompositeWorldCommandPort {
        ports = List.copyOf(Objects.requireNonNull(ports));
        for (var p : ports) Objects.requireNonNull(p);
    }

    @Override public void apply(FrameContext frame, GameState state, List<Object> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(events);
        for (var p : ports) p.apply(frame, state, events);
    }
}
