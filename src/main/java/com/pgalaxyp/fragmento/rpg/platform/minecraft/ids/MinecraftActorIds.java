package com.pgalaxyp.fragmento.rpg.platform.minecraft.ids;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import java.util.UUID;

public final class MinecraftActorIds {

    public static ActorId fromUuid(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException();
        }
        long v = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
        v = v & Long.MAX_VALUE;
        if (v == 0L) {
            v = 1L;
        }
        return new ActorId(v);
    }

    private MinecraftActorIds() {}
}