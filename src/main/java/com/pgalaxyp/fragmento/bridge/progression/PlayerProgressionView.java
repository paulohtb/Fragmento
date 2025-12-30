package com.pgalaxyp.fragmento.bridge.progression;

import java.util.UUID;

public interface PlayerProgressionView {

    int level(UUID playerId);

    long version(UUID playerId);

    String label(UUID playerId);
}