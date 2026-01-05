package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record ExecutionState(
        boolean active,
        ExecutionKind kind,
        Time endsAt
) {

    public static ExecutionState idle() {
        return new ExecutionState(false, ExecutionKind.NONE, Time.ofTicks(0L));
    }

    public Time expectedEndAt() {
        return endsAt;
    }

    public ExecutionState stop() {
        return new ExecutionState(false, ExecutionKind.NONE, Time.ofTicks(0L));
    }
}