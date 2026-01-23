package com.pgalaxyp.fragmento.combat.actorModule.api;

public interface ActorService {
    boolean track(ActorId actorId);
    boolean isTracked(ActorId actorId);
}