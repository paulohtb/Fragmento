package com.pgalaxyp.fragmento.rpg.core.boot;

import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.loop.UpdateScheduler;
import com.pgalaxyp.fragmento.rpg.gameplay.combat.basic.ComboResolver;
import com.pgalaxyp.fragmento.rpg.gameplay.damage.DamageResolver;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectSpawnSystem;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectSystem;
import com.pgalaxyp.fragmento.rpg.gameplay.input.InputRouter;

public final class RpgWiring implements ModuleWiring {

    private final InputRouter input;
    private final ComboResolver combo;
    private final EffectSpawnSystem effectSpawn;
    private final EffectSystem effects;
    private final DamageResolver damage;

    public RpgWiring(
            InputRouter input,
            ComboResolver combo,
            EffectSpawnSystem effectSpawn,
            EffectSystem effects,
            DamageResolver damage
    ) {
        this.input = input;
        this.combo = combo;
        this.effectSpawn = effectSpawn;
        this.effects = effects;
        this.damage = damage;
    }

    @Override
    public void registerSystems(UpdateScheduler scheduler, TickBus bus) {
        scheduler
                .register(input)
                .register(combo)
                .register(effectSpawn)
                .register(effects)
                .register(damage);
    }
}