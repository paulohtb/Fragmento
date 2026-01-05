package com.pgalaxyp.fragmento.rpg.platform.api.player;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public interface PlayerView {
    long actorId();
    Vec3 position();
    Vec3 lookDirection();
}