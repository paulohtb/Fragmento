package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.input.api.InputContext;

public interface FrameClock {
    long frameId(InputContext context);
    default void tick() {}
}