package com.pgalaxyp.fragmento.rpg.engine;

import com.pgalaxyp.fragmento.rpg.core.time.*;
import com.pgalaxyp.fragmento.rpg.ports.dto.*;
import com.pgalaxyp.fragmento.rpg.core.events.event.*;
import java.util.*;

public record FrameOutput(FrameContext frame, GameSnapshot snapshot, List<DomainEvent> events) {

    public FrameOutput {
        Objects.requireNonNull(frame, "frame cannot be null");
        Objects.requireNonNull(snapshot, "snapshot cannot be null");
        Objects.requireNonNull(events, "events cannot be null");
        events = List.copyOf(events);
    }
}