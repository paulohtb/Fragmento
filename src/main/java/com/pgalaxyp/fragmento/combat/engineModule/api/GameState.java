package com.pgalaxyp.fragmento.combat.engineModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;

import java.util.Objects;

public record GameState(FrameContext frame) {
    public GameState { Objects.requireNonNull(frame); }
    public static GameState empty(FrameContext frame) { return new GameState(Objects.requireNonNull(frame)); }
}
