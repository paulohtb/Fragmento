package com.pgalaxyp.fragmento.common.sync;

import java.util.UUID;

public interface VersionedStateStore<T extends VersionedState> {

    T snapshot(UUID playerId);

    void invalidate(UUID playerId);
}