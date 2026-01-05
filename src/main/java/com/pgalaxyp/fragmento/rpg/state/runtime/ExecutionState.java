package com.pgalaxyp.fragmento.rpg.state.runtime;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

public record ExecutionState(
        boolean active,
        ExecutionKind kind,
        long execId,
        Time startedAt,
        Time expectedEndAt
) {

    public static ExecutionState idle() {
        return new ExecutionState(
                false,
                ExecutionKind.NONE,
                0L,
                Time.ofTicks(0L),
                Time.ofTicks(0L)
        );
    }

    public ExecutionState stop() {
        return new ExecutionState(
                false,
                ExecutionKind.NONE,
                execId,
                startedAt,
                expectedEndAt
        );
    }
}