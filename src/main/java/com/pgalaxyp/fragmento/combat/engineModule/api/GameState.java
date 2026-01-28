package com.pgalaxyp.fragmento.combat.engineModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.util.*;

public record GameState(FrameContext frame, ActorView actors) {
    public GameState {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
    }

    public Optional<ActorState> findActor(ActorId actorId) {
        return actors.findActor(actorId);
    }

    public ActorState actor(ActorId actorId) {
        return actors.actor(actorId);
    }

    public static GameState empty(FrameContext frame) {
        return new GameState(Objects.requireNonNull(frame), ActorView.empty());
    }
}