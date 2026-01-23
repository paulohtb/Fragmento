package com.pgalaxyp.fragmento.combat.targetingModule.port;

import com.pgalaxyp.fragmento.combat.util.Vec3d;

public sealed interface RaycastHit permits RaycastEntityHit, RaycastBlockHit {
    Vec3d hitPosition();
    double distance();
}