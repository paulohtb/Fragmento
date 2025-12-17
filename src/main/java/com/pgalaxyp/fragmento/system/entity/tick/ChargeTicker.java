package com.pgalaxyp.fragmento.system.entity.tick;

import com.pgalaxyp.fragmento.system.charge.ChargeInstance;
import com.pgalaxyp.fragmento.system.charge.ChargeSystem;
import com.pgalaxyp.fragmento.system.temporal.ScheduledTask;

import java.util.UUID;

public final class ChargeTicker extends ScheduledTask {

    private final ChargeSystem charges;
    private final UUID instrumentId;
    private final int max;

    public ChargeTicker(
            long startTick,
            ChargeSystem charges,
            UUID instrumentId,
            int max
    ) {
        super(startTick, 20);
        this.charges = charges;
        this.instrumentId = instrumentId;
        this.max = max;
    }

    @Override
    protected void execute(long now) {
        ChargeInstance c = charges.getOrCreate(instrumentId, max);
        if (!c.isCharged()) {
            c.increment();
        }
    }
}