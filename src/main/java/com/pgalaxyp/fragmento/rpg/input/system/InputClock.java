package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.input.api.ModInputContext;

public interface InputClock {
    long frameId(ModInputContext context);
}