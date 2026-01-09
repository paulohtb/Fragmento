package com.pgalaxyp.fragmento.rpg.core.domain.spec;

public record HomingMagicSpec(
        int lifetimeFrames
) implements EffectVisualSpec {
    public HomingMagicSpec {
        if (lifetimeFrames <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public static HomingMagicSpec defaultSpec() {
        return new HomingMagicSpec(20);
    }
}