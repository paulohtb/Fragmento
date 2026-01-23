package com.pgalaxyp.fragmento.combat.targetingModule.api;

@FunctionalInterface
public interface TargetingService {
    TargetResult resolve(TargetingRequest request);
}