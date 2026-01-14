package com.pgalaxyp.fragmento.rpg.combo.internal;

import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.combo.state.*;
import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.combo.skill.*;
import com.pgalaxyp.fragmento.rpg.combo.registry.*;
import com.pgalaxyp.fragmento.rpg.combo.resolver.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public final class ComboEngine implements ComboService {

    private final ComboTracker tracker;
    private final ComboCatalog catalog;
    private final ComboSkillResolver skills;
    private final ComboResolver resolver;

    public ComboEngine(ComboTracker tracker, ComboCatalog catalog, ComboSkillResolver skills) {
        if (tracker == null || catalog == null || skills == null) {
            throw new IllegalArgumentException();
        }

        this.tracker = tracker;
        this.catalog = catalog;
        this.skills = skills;
        this.resolver = new ComboResolver();
    }

    @Override
    public ComboDecision decide(ActorId actorId, WeaponId weaponId, ComboInput input) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(input);

        Optional<ComboDefinition> baseOpt = catalog.baseFor(weaponId);
        if (baseOpt.isEmpty()) {
            tracker.clear(actorId);
            return ComboDecision.reject();
        }

        ComboDefinition base = baseOpt.get();
        ComboPattern effective = skills.resolve(actorId, weaponId, base.pattern());
        resolver.validatePattern(effective);
        ComboDefinition def = new ComboDefinition(base.actionKey(), effective);

        Optional<ComboState> stOpt = tracker.get(actorId);
        if (stOpt.isPresent()) {
            ComboState st = stOpt.get();
            if (!st.weaponId().equals(weaponId)) {
                tracker.clear(actorId);
                stOpt = Optional.empty();
            }
        }

        Optional<Integer> nextOpt = resolver.nextStepIndex(stOpt, def, input);
        if (nextOpt.isEmpty()) {
            return ComboDecision.reject();
        }

        int nextIndex = nextOpt.get();
        int total = def.stepsTotal();
        ComboStep step = def.pattern().step(nextIndex);

        ComboState nextState;
        if (stOpt.isEmpty()) {
            nextState = new ComboState(def.actionKey(), weaponId, 0, total);
        } else if (nextIndex == 0) {
            nextState = new ComboState(def.actionKey(), weaponId, 0, total);
        } else {
            ComboState prev = stOpt.get();
            if (prev.stepIndex() + 1 != nextIndex) {
                return ComboDecision.reject();
            }
            nextState = prev.advance();
        }
        if (nextIndex == total - 1) {
            tracker.clear(actorId);
        } else {
            tracker.put(actorId, nextState);
        }

        return ComboDecision.accept(def.actionKey(), weaponId, nextIndex, total, step);
    }

    @Override
    public void reset(ActorId actorId) {
        Objects.requireNonNull(actorId);
        tracker.clear(actorId);
    }

    @Override
    public Optional<ComboState> stateOf(ActorId actorId) {
        Objects.requireNonNull(actorId);
        return tracker.get(actorId);
    }
}