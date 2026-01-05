package com.pgalaxyp.fragmento.rpg.core.boot;

import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.UpdateScheduler;

public interface ModuleWiring {
    void registerSystems(UpdateScheduler scheduler, TickBus bus);
}