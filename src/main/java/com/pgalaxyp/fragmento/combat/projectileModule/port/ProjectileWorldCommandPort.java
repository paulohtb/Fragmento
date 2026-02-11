package com.pgalaxyp.fragmento.combat.projectileModule.port;

import com.pgalaxyp.fragmento.combat.projectileModule.event.ProjectileSpawned;

@FunctionalInterface
public interface ProjectileWorldCommandPort {
    void apply(ProjectileSpawned projectile);
}