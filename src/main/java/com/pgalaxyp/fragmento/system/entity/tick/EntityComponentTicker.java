package com.pgalaxyp.fragmento.system.entity.tick;

import com.pgalaxyp.fragmento.system.entity.component.EntityComponent;
import com.pgalaxyp.fragmento.system.entity.component.EntityComponentContainer;

public final class EntityComponentTicker {

    public void tick(EntityComponentContainer container) {
        for (EntityComponent c : container.components()) {
            if (c.isActive()) {
                c.tick();
            }
        }
    }
}