package com.pgalaxyp.fragmento.combat.engineModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorView;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import java.util.Objects;

public record GameState(FrameContext frame, ActorView actors) {
    public GameState {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(actors);
    }

    public static GameState empty(FrameContext frame) {
        return new GameState(Objects.requireNonNull(frame), ActorView.empty());
    }
}