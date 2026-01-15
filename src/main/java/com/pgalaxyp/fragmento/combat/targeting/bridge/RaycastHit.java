package com.pgalaxyp.fragmento.combat.targeting.bridge;

public sealed interface RaycastHit permits RaycastEntityHit, RaycastBlockHit {
    double distance();
}