package com.pgalaxyp.fragmento.platform.sync;

import java.util.UUID;

public interface VersionedStateStore<T extends VersionedState> {

    T snapshot(UUID playerId);

    void invalidate(UUID playerId);
}