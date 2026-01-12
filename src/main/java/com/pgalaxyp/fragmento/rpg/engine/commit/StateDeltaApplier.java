package com.pgalaxyp.fragmento.rpg.engine.commit;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ClassId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ActorSpawned;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboAdvanced;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboEnded;
import com.pgalaxyp.fragmento.rpg.core.events.delta.ComboStarted;
import com.pgalaxyp.fragmento.rpg.core.events.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.ComboState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public final class StateDeltaApplier {

    public static GameState applyAll(GameState base, Iterable<StateDelta> deltas) {
        if (base == null || deltas == null) {
            throw new IllegalArgumentException();
        }
        NavigableMap<ActorId, ActorState> actors = new TreeMap<>(base.actors());
        for (StateDelta delta : deltas) {
            if (delta == null) {
                throw new IllegalArgumentException();
            }
            applyOne(actors, delta);
        }
        return new GameState(base.frame(), actors);
    }

    private static void applyOne(Map<ActorId, ActorState> actors, StateDelta delta) {
        if (delta instanceof ActorSpawned(ActorId actorId, ClassId classId, WeaponId weaponId, int healthHearts, int maxHealthHearts)) {
            if (actors.containsKey(actorId)) {
                return;
            }
            ActorState next = ActorState.withEquippedWeapon(classId, weaponId, healthHearts, maxHealthHearts);
            actors.put(actorId, next);
            return;
        }

        if (delta instanceof ComboStarted(ActorId id, ActionId actionId, WeaponId weaponId, int stepsTotal, long frameId)) {
            ActorState prev = actors.get(id);
            if (prev == null) {
                return;
            }
            if (prev.combo().isPresent()) {
                return;
            }
            if (prev.equippedWeaponId().isEmpty()) {
                return;
            }
            if (!prev.equippedWeaponId().get().equals(weaponId)) {
                return;
            }
            ComboState combo = new ComboState(actionId, weaponId, 0, stepsTotal, frameId);
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), Optional.of(combo), prev.healthHearts(), prev.maxHealthHearts());
            actors.put(id, next);
            return;
        }

        if (delta instanceof ComboAdvanced(ActorId actorId, long stepFrameId)) {
            ActorState prev = actors.get(actorId);
            if (prev == null || prev.combo().isEmpty()) {
                return;
            }
            if (prev.equippedWeaponId().isEmpty()) {
                return;
            }
            ComboState current = prev.combo().get();
            if (!prev.equippedWeaponId().get().equals(current.weaponId())) {
                return;
            }
            int nextIndex = Math.addExact(current.stepIndex(), 1);
            if (nextIndex >= current.stepsTotal()) {
                return;
            }
            ComboState nextCombo = new ComboState(current.actionId(), current.weaponId(), nextIndex, current.stepsTotal(), stepFrameId);
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), Optional.of(nextCombo), prev.healthHearts(), prev.maxHealthHearts());
            actors.put(actorId, next);
            return;
        }

        if (delta instanceof ComboEnded(ActorId actorId, ActionId actionId)) {
            ActorState prev = actors.get(actorId);
            if (prev == null || prev.combo().isEmpty()) {
                return;
            }
            ComboState current = prev.combo().get();
            if (!current.actionId().equals(actionId)) {
                return;
            }
            ActorState next = new ActorState(prev.classId(), prev.equippedWeaponId(), Optional.empty(), prev.healthHearts(), prev.maxHealthHearts());
            actors.put(actorId, next);
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