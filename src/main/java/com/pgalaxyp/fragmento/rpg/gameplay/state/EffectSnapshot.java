package com.pgalaxyp.fragmento.rpg.gameplay.state;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public record EffectSnapshot(
        long effectId,
        String type,
        String stepId,
        Vec3 position,
        Vec3 origin,
        Vec3 aim
) {}