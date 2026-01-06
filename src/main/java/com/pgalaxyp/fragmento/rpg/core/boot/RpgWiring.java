package com.pgalaxyp.fragmento.rpg.core.boot;

import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.UpdateScheduler;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboResolver;
import com.pgalaxyp.fragmento.rpg.gameplay.damage.DamageResolver;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectSpawnSystem;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectSystem;

public final class RpgWiring implements ModuleWiring {

    private final ComboResolver combo;
    private final EffectSpawnSystem effectSpawn;
    private final EffectSystem effects;
    private final DamageResolver damage;

    public RpgWiring(
            ComboResolver combo,
            EffectSpawnSystem effectSpawn,
            EffectSystem effects,
            DamageResolver damage
    ) {
        this.combo = combo;
        this.effectSpawn = effectSpawn;
        this.effects = effects;
        this.damage = damage;
    }

    @Override
    public void registerSystems(UpdateScheduler scheduler, TickBus bus) {
        scheduler
                .register(combo)
                .register(effectSpawn)
                .register(effects)
                .register(damage);
    }
}