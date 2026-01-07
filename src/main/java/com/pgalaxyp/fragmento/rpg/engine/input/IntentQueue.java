package com.pgalaxyp.fragmento.rpg.engine.input;

import java.util.ArrayDeque;
import java.util.Optional;

public final class IntentQueue {

    private final ArrayDeque<Intent> queue = new ArrayDeque<>();

    public void offer(Intent intent) {
        if (intent == null) return;
        queue.addLast(intent);
    }

    public Optional<Intent> poll() {
        return Optional.ofNullable(queue.pollFirst());
    }
}