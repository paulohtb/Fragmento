package com.pgalaxyp.fragmento.rpg.engine.commit;

import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import java.util.ArrayList;
import java.util.List;

public final class StateDeltaMerger {

    public static List<StateDelta> mergeStable(List<StateDelta> first, List<StateDelta> second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException();
        }
        ArrayList<StateDelta> out = new ArrayList<>(Math.addExact(first.size(), second.size()));
        out.addAll(first);
        out.addAll(second);
        return List.copyOf(out);
    }

    private StateDeltaMerger() {}
}