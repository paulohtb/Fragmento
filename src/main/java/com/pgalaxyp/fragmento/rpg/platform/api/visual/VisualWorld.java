package com.pgalaxyp.fragmento.rpg.platform.api.visual;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;

public interface VisualWorld {
    void debugPoint(Vec3 p);
    void debugLine(Vec3 a, Vec3 b);
}