package com.pgalaxyp.fragmento.rpg.gameplay.state;

import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.SnapshottingEffect;
import java.util.ArrayList;
import java.util.Objects;

public final class CombatSnapshotService {

    private final ActorRepository actors;
    private final EffectRepository effects;

    public CombatSnapshotService(ActorRepository actors, EffectRepository effects) {
        this.actors = Objects.requireNonNull(actors);
        this.effects = Objects.requireNonNull(effects);
    }

    public CombatSnapshot build(long actorId, long version) {
        var combo = actors.combo(actorId);
        var comboSnap = new ComboSnapshot(
                combo.index(),
                combo.executing(),
                combo.activeStepId(),
                combo.lastSpawnSide()
        );

        var list = new ArrayList<EffectSnapshot>();
        for (var e : effects.snapshot().values()) {
            if (e instanceof SnapshottingEffect se && se.ownerActorId() == actorId) {
                list.add(se.snapshot());
            }
        }

        return new CombatSnapshot(actorId, version, comboSnap, list);
    }
}