package com.pgalaxyp.fragmento.bridge.sync;

import java.util.UUID;

public interface VersionedStatePublisher<T extends VersionedState> {

    void publish(UUID playerId, T state);
}