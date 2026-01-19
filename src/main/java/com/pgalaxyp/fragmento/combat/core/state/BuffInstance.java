package com.pgalaxyp.fragmento.combat.core.state;

import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public record BuffInstance(BuffId buffId, ActorId actorId, long startFrame, long endFrame) {
    public BuffInstance {
        Objects.requireNonNull(buffId);
        Objects.requireNonNull(actorId);
        if (startFrame < 0 || endFrame <= startFrame) throw new IllegalArgumentException();
    }
    public boolean activeAt(long frame) { return frame >= startFrame && frame < endFrame; }
}
