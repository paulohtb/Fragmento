package com.pgalaxyp.fragmento.cosmetics.common.validation;

import java.util.UUID;

public interface PlayerLevelResolver {
    int resolveLevel(UUID playerId);
}