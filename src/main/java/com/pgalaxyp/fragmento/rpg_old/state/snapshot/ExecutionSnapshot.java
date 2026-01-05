package com.pgalaxyp.fragmento.rpg_old.state.snapshot;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.ExecutionKind;

public record ExecutionSnapshot(
        boolean active,
        ExecutionKind kind,
        Time endsAt
) {

    public static ExecutionSnapshot idle() {
        return new ExecutionSnapshot(false, ExecutionKind.NONE, Time.ofTicks(0L));
    }
}