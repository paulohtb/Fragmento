package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.missile.MagicMissileId;

public record MissileExpired(
        MagicMissileId missileId
) {}