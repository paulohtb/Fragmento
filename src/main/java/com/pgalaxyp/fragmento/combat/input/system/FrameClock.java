package com.pgalaxyp.fragmento.combat.input.system;

import com.pgalaxyp.fragmento.combat.input.api.InputContext;

public interface FrameClock {
    long frameId(InputContext context);
    default void tick() {}
}