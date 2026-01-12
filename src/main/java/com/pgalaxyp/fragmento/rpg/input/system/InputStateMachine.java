package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import java.util.NavigableMap;
import java.util.TreeMap;

public final class InputStateMachine {

    private final NavigableMap<ActorId, ActorInputState> byActor = new TreeMap<>();
    private final int primaryDebounceFrames;

    public InputStateMachine(int primaryDebounceFrames) {
        if (primaryDebounceFrames <= 0) {
            throw new IllegalArgumentException();
        }
        this.primaryDebounceFrames = primaryDebounceFrames;
    }

    public boolean allowPrimaryAction(ActorId actorId, long frameId) {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
        if (frameId < 0) {
            throw new IllegalArgumentException();
        }

        ActorInputState st = byActor.get(actorId);
        if (st == null) {
            st = new ActorInputState(0L);
            byActor.put(actorId, st);
        }

        if (frameId < st.nextAllowedPrimaryFrameId) {
            return false;
        }

        long next = Math.addExact(frameId, (long) primaryDebounceFrames);
        st.nextAllowedPrimaryFrameId = next;
        return true;
    }

    public void clearActor(ActorId actorId) {
        if (actorId == null) {
            throw new IllegalArgumentException();
        }
        byActor.remove(actorId);
    }

    private static final class ActorInputState {
        private long nextAllowedPrimaryFrameId;

        private ActorInputState(long nextAllowedPrimaryFrameId) {
            this.nextAllowedPrimaryFrameId = nextAllowedPrimaryFrameId;
        }
    }
}