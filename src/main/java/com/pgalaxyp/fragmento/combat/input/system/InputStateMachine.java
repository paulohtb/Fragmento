package com.pgalaxyp.fragmento.combat.input.system;

import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public final class InputStateMachine {

    private final NavigableMap<ActorId, ActorState> byActor = new TreeMap<>();
    private final int primaryDebounceFrames;

    public InputStateMachine(int primaryDebounceFrames) {
        if (primaryDebounceFrames <= 0) {
            throw new IllegalArgumentException();
        }
        this.primaryDebounceFrames = primaryDebounceFrames;
    }

    public Decision decidePrimaryAction(ActorId actorId, long frameId) {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
        if (frameId < 0) {
            throw new IllegalArgumentException();
        }

        ActorState st = byActor.get(actorId);
        if (st == null) {
            st = new ActorState();
            byActor.put(actorId, st);
        }

        if (st.lastDecisionFrameId == frameId) {
            return Decision.ALREADY_DECIDED_THIS_FRAME;
        }

        if (frameId < st.nextAllowedPrimaryFrameId) {
            st.lastDecisionFrameId = frameId;
            return Decision.BLOCKED_BY_DEBOUNCE;
        }

        st.lastDecisionFrameId = frameId;
        st.nextAllowedPrimaryFrameId = frameId + primaryDebounceFrames;
        return Decision.ALLOWED;
    }

    public enum Decision {
        ALLOWED,
        BLOCKED_BY_DEBOUNCE,
        ALREADY_DECIDED_THIS_FRAME
    }

    private static final class ActorState {
        private long nextAllowedPrimaryFrameId;
        private long lastDecisionFrameId = Long.MIN_VALUE;
    }
}