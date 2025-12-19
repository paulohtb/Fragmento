package com.pgalaxyp.fragmento.system.charge;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ChargeSystem {

    private final Map<UUID, ChargeInstance> byInstrument = new HashMap<>();

    public ChargeInstance getOrCreate(UUID instrumentId, int max) {
        return byInstrument.computeIfAbsent(
                instrumentId,
                id -> new ChargeInstance(id, max)
        );
    }
}