package com.pgalaxyp.fragmento.combat.platformModule.minecraft;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamageWorldCommandPort;
import java.util.*;

public record McDamageWorldCommandAdapter(DamageWorldCommandPort damage) implements WorldCommandPort {
    public McDamageWorldCommandAdapter { Objects.requireNonNull(damage); }

    @Override public void apply(FrameContext frame, List<FrameEvent> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(events);
        for (var e : events) if (e instanceof DamageApplied d) damage.apply(d);
    }
}