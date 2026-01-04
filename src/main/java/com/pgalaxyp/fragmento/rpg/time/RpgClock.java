package com.pgalaxyp.fragmento.rpg.time;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;

@FunctionalInterface
public interface RpgClock {
    Time now();
}