package com.pgalaxyp.fragmento.combat.targeting.port;

import com.pgalaxyp.fragmento.combat.targeting.api.Vec3d;

public sealed interface RaycastHit permits RaycastEntityHit, RaycastBlockHit {
    Vec3d hitPosition();
    double distance();
}