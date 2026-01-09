package com.pgalaxyp.fragmento.rpg.engine.commit;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.event.delta.ComboAdvanced;
import com.pgalaxyp.fragmento.rpg.core.event.delta.ComboEnded;
import com.pgalaxyp.fragmento.rpg.core.event.delta.ComboStarted;
import com.pgalaxyp.fragmento.rpg.core.event.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public final class StateDeltaApplier {

    public static GameState applyAll(GameState base, Iterable<StateDelta> deltas) {
        if (base == null || deltas == null) {
            throw new IllegalArgumentException();
        }
        Map<ActorId, ActorState> actors = new LinkedHashMap<>(base.actors());
        for (StateDelta delta : deltas) {
            if (delta == null) {
                throw new IllegalArgumentException();
            }
            applyOne(actors, delta);
        }
        return new GameState(base.frame(), actors);
    }

    private static void applyOne(Map<ActorId, ActorState> actors, StateDelta delta) {
        if (delta instanceof ComboStarted(ActorId actorId, ActionId actionId, WeaponId weaponId, int stepsTotal)) {
            ActorState prev = actors.get(actorId);
            if (prev == null) {
                return;
            }
            ComboState combo = new ComboState(actionId, weaponId, 0, stepsTotal);
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), Optional.of(combo), prev.healthHearts(), prev.maxHealthHearts());
            actors.put(actorId, next);
            return;
        }

        if (delta instanceof ComboAdvanced(ActorId actorId)) {
            ActorState prev = actors.get(actorId);
            if (prev == null || prev.combo().isEmpty()) {
                return;
            }
            ComboState current = prev.combo().get();
            int nextIndex = current.stepIndex() + 1;
            if (nextIndex >= current.stepsTotal()) {
                return;
            }
            ComboState nextCombo = new ComboState(current.actionId(), current.weaponId(), nextIndex, current.stepsTotal());
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), Optional.of(nextCombo), prev.healthHearts(), prev.maxHealthHearts());
            actors.put(actorId, next);
            return;
        }

        if (delta instanceof ComboEnded d) {
            ActorState prev = actors.get(d.actorId());
            if (prev == null) {
                return;
            }
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), Optional.empty(), prev.healthHearts(), prev.maxHealthHearts());
            actors.put(d.actorId(), next);
            return;
        }

        if (delta instanceof DamageApplied(ActorId targetActorId, int hearts)) {
            ActorState prev = actors.get(targetActorId);
            if (prev == null) {
                return;
            }
            int nextHealth = Math.addExact(prev.healthHearts(), Math.negateExact(hearts));
            if (nextHealth < 0) {
                nextHealth = 0;
            }
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), prev.combo(), nextHealth, prev.maxHealthHearts());
            actors.put(targetActorId, next);
        }
    }

    private StateDeltaApplier() {}
}