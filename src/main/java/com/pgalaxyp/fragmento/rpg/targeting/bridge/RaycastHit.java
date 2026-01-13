package com.pgalaxyp.fragmento.rpg.targeting.bridge;

public sealed interface RaycastHit permits RaycastEntityHit, RaycastBlockHit {
    double distance();
}