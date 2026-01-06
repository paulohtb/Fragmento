package com.pgalaxyp.fragmento.rpg.engine.missile;

import com.pgalaxyp.fragmento.rpg.core.domain.damage.DamageRequest;
import com.pgalaxyp.fragmento.rpg.core.domain.damage.DamageType;
import com.pgalaxyp.fragmento.rpg.core.math.Vec3;
import com.pgalaxyp.fragmento.rpg.core.rule.combo.ComboStepTriggered;
import com.pgalaxyp.fragmento.rpg.engine.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.engine.loop.Updatable;
import com.pgalaxyp.fragmento.rpg.platform.api.targeting.TargetQuery;
import com.pgalaxyp.fragmento.rpg.platform.api.targeting.TargetResult;
import com.pgalaxyp.fragmento.rpg.platform.api.targeting.TargetingPort;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public final class MagicMissileEngineSystem implements Updatable {

    private final List<ActiveMagicMissile> missiles = new ArrayList<>();
    private final TargetingPort targeting;
    private final Random rng;

    public MagicMissileEngineSystem(TargetingPort targeting, Random rng, TickBus bus) {
        this.targeting = targeting;
        this.rng = rng;

        bus.subscribe(ComboStepTriggered.class, this::onComboStep);
    }

    private void onComboStep(ComboStepTriggered e) {
        var origin = new Vec3(0, 0, 0);
        var direction = new Vec3(0, 0, 1);

        var query = new TargetQuery(
                e.actorId(),
                origin,
                direction,
                2.0,
                20.0
        );

        var target = targeting.resolve(query)
                .map(TargetResult::target)
                .orElse(null);

        if (target == null || !target.isReal()) return;

        var lifetime = 0.5 + rng.nextDouble() * 0.25;

        missiles.add(new ActiveMagicMissile(
                e.actorId(),
                target.actorIdOrZero(),
                3.0,
                lifetime,
                0.0
        ));
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
        Iterator<ActiveMagicMissile> it = missiles.iterator();

        while (it.hasNext()) {
            var m = it.next().advance(tick.deltaSeconds());

            if (m.expired()) {
                bus.publish(new DamageRequest(
                        m.sourceActorId(),
                        m.targetActorId(),
                        m.damage(),
                        DamageType.MAGIC
                ));
                it.remove();
            } else {
                it.remove();
                missiles.add(m);
            }
        }
    }
}