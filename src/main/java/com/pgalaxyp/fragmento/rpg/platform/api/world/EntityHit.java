package com.pgalaxyp.fragmento.rpg.platform.api.world;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public record EntityHit(long actorId, Vec3 hitPos, double distance) {}