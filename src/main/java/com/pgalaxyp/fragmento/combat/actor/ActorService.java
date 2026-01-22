package com.pgalaxyp.fragmento.combat.actor;

public interface ActorService {
    boolean track(ActorId actorId);
    boolean isTracked(ActorId actorId);
}