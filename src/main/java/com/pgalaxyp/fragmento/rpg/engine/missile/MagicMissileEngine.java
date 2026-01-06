package com.pgalaxyp.fragmento.rpg.engine.missile;

import com.pgalaxyp.fragmento.rpg.core.domain.event.MissileSpawned;
import com.pgalaxyp.fragmento.rpg.core.domain.missile.MagicMissileId;
import com.pgalaxyp.fragmento.rpg.core.rule.missile.MagicMissileRule;
import com.pgalaxyp.fragmento.rpg.core.state.missile.MagicMissileState;
import com.pgalaxyp.fragmento.rpg.engine.loop.GameTick;
import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.engine.loop.Updatable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class MagicMissileEngine implements Updatable {

    private final Map<MagicMissileId, MagicMissileState> missiles = new HashMap<>();
    private final MagicMissileRule rule = new MagicMissileRule();

    public MagicMissileEngine(TickBus bus) {
        bus.subscribe(MissileSpawned.class, this::onSpawn);
    }

    private void onSpawn(MissileSpawned e) {
        missiles.put(
                e.missileId(),
                new MagicMissileState(
                        e.missileId(),
                        e.sourceActorId(),
                        e.targetActorId(),
                        3.0,
                        0.6
                )
        );
    }

    @Override
    public void update(GameTick tick, TickBus bus) {
        Iterator<MagicMissileState> it = missiles.values().iterator();

        while (it.hasNext()) {
            var m = it.next();
            var result = rule.tick(m, tick.deltaSeconds());

            for (var ev : result.events()) {
                bus.publish(ev);
            }

            if (result.nextState() == null) {
                it.remove();
            } else {
                it.remove();
                missiles.put(result.nextState().id(), result.nextState());
            }
        }
    }
}