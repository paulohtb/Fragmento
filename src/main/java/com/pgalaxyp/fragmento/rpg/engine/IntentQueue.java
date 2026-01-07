package com.pgalaxyp.fragmento.rpg.engine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public final class IntentQueue {

    private final Deque<Intent> queue = new ArrayDeque<>();

    public void push(Intent intent) {
        queue.addLast(intent);
    }

    public List<Intent> drain() {
        List<Intent> out = new ArrayList<>();
        while (!queue.isEmpty()) {
            out.add(queue.removeFirst());
        }
        return out;
    }
}