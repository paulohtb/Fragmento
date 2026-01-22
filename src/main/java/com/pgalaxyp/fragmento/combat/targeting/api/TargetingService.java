package com.pgalaxyp.fragmento.combat.targeting.api;

@FunctionalInterface
public interface TargetingService {
    TargetResult resolve(TargetingRequest request);
}