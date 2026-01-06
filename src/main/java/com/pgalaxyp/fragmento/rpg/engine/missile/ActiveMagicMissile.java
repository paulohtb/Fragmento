package com.pgalaxyp.fragmento.rpg.engine.missile;

public record ActiveMagicMissile(
        long sourceActorId,
        long targetActorId,
        double damage,
        double lifetimeSeconds,
        double elapsedSeconds
) {
    public ActiveMagicMissile advance(double delta) {
        return new ActiveMagicMissile(
                sourceActorId,
                targetActorId,
                damage,
                lifetimeSeconds,
                elapsedSeconds + delta
        );
    }

    public boolean expired() {
        return elapsedSeconds >= lifetimeSeconds;
    }
}