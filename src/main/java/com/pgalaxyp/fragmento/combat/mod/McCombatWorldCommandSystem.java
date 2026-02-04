package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.damageModule.minecraft.McDamageWorldCommandPort;
import java.util.*;

public record McCombatWorldCommandSystem(McDamageWorldCommandPort damage) implements WorldCommandPort {
    public McCombatWorldCommandSystem { Objects.requireNonNull(damage); }

    @Override public void apply(FrameContext frame, GameState state, List<FrameEvent> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(events);
        for (var e : events) if (e instanceof DamageApplied d) damage.apply(d);
    }
}