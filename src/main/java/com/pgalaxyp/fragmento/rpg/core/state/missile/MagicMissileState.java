package com.pgalaxyp.fragmento.rpg.core.state.missile;

import com.pgalaxyp.fragmento.rpg.core.domain.missile.MagicMissileId;

public record MagicMissileState(
        MagicMissileId id,
        long sourceActorId,
        long targetActorId,
        double damage,
        double remainingSeconds
) {
    public MagicMissileState tick(double deltaSeconds) {
        return new MagicMissileState(
                id,
                sourceActorId,
                targetActorId,
                damage,
                remainingSeconds - deltaSeconds
        );
    }

    public boolean expired() {
        return remainingSeconds <= 0.0;
    }
}