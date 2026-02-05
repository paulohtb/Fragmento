package com.pgalaxyp.fragmento.combat.damageModule.minecraft;

import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamageWorldCommandPort;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.util.*;

public record McDamageWorldCommandSystem(DamageWorldCommandPort damage) implements WorldCommandPort {
    public McDamageWorldCommandSystem { Objects.requireNonNull(damage); }

    @Override public void apply(FrameContext frame, GameState state, List<Object> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(events);
        for (var e : events) if (e instanceof DamageApplied d) damage.apply(d);
    }
}
