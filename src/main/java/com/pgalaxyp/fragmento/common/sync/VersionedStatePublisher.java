package com.pgalaxyp.fragmento.common.sync;

import java.util.UUID;

public interface VersionedStatePublisher<T extends VersionedState> {

    void publish(UUID playerId, T state);
}