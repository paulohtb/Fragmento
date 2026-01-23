package com.pgalaxyp.fragmento.combat.actor.api;

public interface ActorService {
    boolean track(ActorId actorId);
    boolean isTracked(ActorId actorId);
}