package com.pgalaxyp.fragmento.combat.damageModule.minecraft;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.engineModule.api.GameState;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import java.util.*;

public final class McDamageWorldCommandSystem implements WorldCommandPort {
    private final McDamageWorldCommandPort damage;

    public McDamageWorldCommandSystem(McDamageWorldCommandPort damage) {
        this.damage = Objects.requireNonNull(damage);
    }

    @Override public void apply(FrameContext frame, GameState state, List<FrameEvent> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(events);
        for (FrameEvent e : events) if (e instanceof DamageApplied d) damage.apply(d);
    }
}