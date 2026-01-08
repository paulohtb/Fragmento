package com.pgalaxyp.fragmento.rpg.engine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import com.pgalaxyp.fragmento.rpg.core.domain.intent.DomainIntent;

public final class IntentQueue implements IntentSource {

    private final Deque<DomainIntent> queue = new ArrayDeque<>();

    public void push(DomainIntent intent) {
        queue.addLast(intent);
    }

    @Override
    public List<DomainIntent> drain() {
        List<DomainIntent> out = new ArrayList<>();
        while (!queue.isEmpty()) {
            out.add(queue.removeFirst());
        }
        return out;
    }
}