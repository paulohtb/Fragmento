package com.pgalaxyp.fragmento.rpg.state.snapshot;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.runtime.ExecutionKind;

public record ExecutionSnapshot(
        boolean active,
        ExecutionKind kind,
        long execId,
        Time startedAt,
        Time expectedEndAt
) {
    public static ExecutionSnapshot idle() {
        return new ExecutionSnapshot(false, ExecutionKind.NONE, 0L, Time.ofTicks(0), Time.ofTicks(0));
    }
}