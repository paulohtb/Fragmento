package com.pgalaxyp.fragmento.rpg.core.domain.effect;

public record MagicMissileEffect(
        double damage,
        double speed,
        double lifetime
) implements EffectDef {}