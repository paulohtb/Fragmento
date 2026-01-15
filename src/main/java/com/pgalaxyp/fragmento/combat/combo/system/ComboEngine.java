package com.pgalaxyp.fragmento.combat.combo.system;

import com.pgalaxyp.fragmento.combat.combo.api.*;
import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.combo.skill.*;
import com.pgalaxyp.fragmento.combat.combo.state.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import java.util.*;

public final class ComboEngine implements ComboService {

    private final ComboTracker tracker;
    private final ComboDefinitionSource source;
    private final ComboSkillResolver skills;
    private final ComboResolver resolver = new ComboResolver();

    public ComboEngine(ComboTracker tracker, ComboDefinitionSource source, ComboSkillResolver skills) {
        this.tracker = Objects.requireNonNull(tracker);
        this.source = Objects.requireNonNull(source);
        this.skills = Objects.requireNonNull(skills);
    }

    @Override
    public ComboResult decide(ActorId actorId, WeaponId weaponId, ComboInput input) {
        var base = source.baseFor(weaponId).orElse(null);
        if (base == null) {
            tracker.clear(actorId);
            return ComboResult.reject();
        }

        ComboPattern pattern = skills.resolve(actorId, weaponId, base.pattern());
        Optional<ComboState> st = tracker.get(actorId);
        Optional<Integer> next = resolver.nextStepIndex(st, base.comboId(), pattern, input);
        if (next.isEmpty()) { return ComboResult.reject(); }

        int index = next.get();
        boolean start = index == 0;
        boolean end = index == pattern.size() - 1;
        if (end) {
            tracker.clear(actorId);
        } else {
            tracker.put(actorId, start ? new ComboState(base.comboId(), 0, pattern.size()) : st.orElseThrow().advance());
        }

        return new ComboResult.Progress(base.comboId(), index, pattern.size(), pattern.step(index), start, end);
    }

    @Override
    public void reset(ActorId actorId) { tracker.clear(actorId); }

    @Override
    public Optional<ComboState> stateOf(ActorId actorId) { return tracker.get(actorId); }
}