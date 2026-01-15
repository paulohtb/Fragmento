package com.pgalaxyp.fragmento.combat.engine;

import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.ports.dto.*;
import com.pgalaxyp.fragmento.combat.event.*;
import java.util.*;

public record FrameOutput(FrameContext frame, GameSnapshot snapshot, List<DomainEvent> events) {

    public FrameOutput {
        Objects.requireNonNull(frame, "frame cannot be null");
        Objects.requireNonNull(snapshot, "snapshot cannot be null");
        Objects.requireNonNull(events, "events cannot be null");
        events = List.copyOf(events);
    }
}