package com.pgalaxyp.fragmento.combat.random;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import java.util.Objects;
import java.util.Optional;

public record GameState(FrameContext frame, ActorView actors) {
    public GameState {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
    }

    public Optional<ActorState> findActor(ActorId actorId) { return actors.findActor(actorId); }

    public ActorState actor(ActorId actorId) { return actors.actor(actorId); }

    public static GameState empty(FrameContext frame) { return new GameState(frame, ActorView.empty()); }
}