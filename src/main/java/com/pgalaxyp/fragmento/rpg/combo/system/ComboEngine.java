package com.pgalaxyp.fragmento.rpg.combo.system;

import com.pgalaxyp.fragmento.rpg.content.*;
import com.pgalaxyp.fragmento.rpg.combo.api.*;
import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.combo.state.*;
import com.pgalaxyp.fragmento.rpg.combo.skill.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;
import java.util.*;

public final class ComboEngine implements ComboService {

    private final ComboTracker tracker;
    private final ComboCatalog catalog;
    private final ComboSkillResolver skills;
    private final ComboResolver resolver = new ComboResolver();

    public ComboEngine(ComboTracker tracker, ComboCatalog catalog, ComboSkillResolver skills) {
        if (tracker == null || catalog == null || skills == null) throw new IllegalArgumentException();
        this.tracker = tracker;
        this.catalog = catalog;
        this.skills = skills;
    }

    @Override
    public ComboResult decide(ActorId actorId, WeaponId weaponId, ComboInput input) {
        if (actorId == null || weaponId == null || input == null) throw new IllegalArgumentException();

        var baseOpt = catalog.baseFor(weaponId);
        if (baseOpt.isEmpty()) {
            tracker.clear(actorId);
            return ComboResult.reject();
        }

        var base = baseOpt.get();
        ComboPattern effective = skills.resolve(actorId, weaponId, base.pattern());

        Optional<ComboState> stOpt = tracker.get(actorId);
        if (stOpt.isPresent() && !stOpt.get().equals(weaponId)) {
            tracker.clear(actorId);
            stOpt = Optional.empty();
        }

        Optional<Integer> nextOpt = resolver.nextStepIndex(stOpt, base.comboId(), effective, input);
        if (nextOpt.isEmpty()) return ComboResult.reject();

        int nextIndex = nextOpt.get();
        int total = effective.size();
        ComboStep step = effective.step(nextIndex);

        boolean start = nextIndex == 0;
        boolean end = nextIndex == total - 1;

        if (end) tracker.clear(actorId);
        else tracker.put(actorId, start ? new ComboState(base.comboId(), 0, total) : stOpt.orElseThrow().advance());

        return new ComboResult.Progress(base.comboId(), nextIndex, total, step, start, end);
    }

    @Override
    public void reset(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        tracker.clear(actorId);
    }

    @Override
    public Optional<ComboState> stateOf(ActorId actorId) {
        if (actorId == null) throw new IllegalArgumentException();
        return tracker.get(actorId);
    }
}