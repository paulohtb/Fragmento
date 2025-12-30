package com.pgalaxyp.fragmento.tier.api;

import java.util.UUID;

public interface PlayerProgressionView {

    int level(UUID playerId);

    long version(UUID playerId);

    String label(UUID playerId);
}