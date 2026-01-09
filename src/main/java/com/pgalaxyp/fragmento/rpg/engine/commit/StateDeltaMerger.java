package com.pgalaxyp.fragmento.rpg.engine.commit;

import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public final class StateDeltaMerger {

    public static List<StateDelta> mergeStableDistinct(List<StateDelta> first, List<StateDelta> second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException();
        }
        LinkedHashSet<StateDelta> set = new LinkedHashSet<>();
        set.addAll(first);
        set.addAll(second);
        return List.copyOf(new ArrayList<>(set));
    }

    private StateDeltaMerger() {}
}