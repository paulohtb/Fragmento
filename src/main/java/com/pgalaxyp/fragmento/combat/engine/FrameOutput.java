package com.pgalaxyp.fragmento.combat.engine;

import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.transport.snapshot.api.*;
import java.util.*;

public record FrameOutput(FrameContext frame, GameSnapshot snapshot) {
    public FrameOutput {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(snapshot);
    }
}
