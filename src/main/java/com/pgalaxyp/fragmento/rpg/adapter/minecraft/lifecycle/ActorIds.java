package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.id.IdGen;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class ActorIds {

    private final IdGen ids;
    private final Map<UUID, Long> idByUuid = new HashMap<>();
    private final Map<Long, UUID> uuidById = new HashMap<>();

    public ActorIds(IdGen ids) {
        this.ids = Objects.requireNonNull(ids);
    }

    public long idFor(UUID uuid) {
        Objects.requireNonNull(uuid);
        var existing = idByUuid.get(uuid);
        if (existing != null) return existing;

        var id = ids.next();
        idByUuid.put(uuid, id);
        uuidById.put(id, uuid);
        return id;
    }

    public Optional<UUID> uuidOf(long actorId) {
        return Optional.ofNullable(uuidById.get(actorId));
    }
}