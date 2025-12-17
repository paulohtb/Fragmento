package com.pgalaxyp.fragmento.client.render;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class ClientChargeState {

    private static final Map<UUID, int[]> STATES = new HashMap<>();

    private ClientChargeState() {
    }

    public static void update(UUID id, int value, int max) {
        STATES.put(id, new int[]{value, max});
    }

    public static int value(UUID id) {
        int[] v = STATES.get(id);
        return v != null ? v[0] : 0;
    }

    public static int max(UUID id) {
        int[] v = STATES.get(id);
        return v != null ? v[1] : 1;
    }
}