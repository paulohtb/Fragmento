package com.pgalaxyp.fragmento.combat.commandModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameCommand;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class ServerCommandQueue implements CommandSourcePort, CommandSinkPort {
    private final ConcurrentLinkedQueue<FrameCommand> queue = new ConcurrentLinkedQueue<>();

    @Override public boolean enqueue(FrameCommand command) { return queue.add(Objects.requireNonNull(command)); }

    @Override public List<FrameCommand> drain() {
        if (queue.isEmpty()) return List.of();
        var out = new ArrayList<FrameCommand>();
        for (FrameCommand c; (c = queue.poll()) != null; ) out.add(c);
        return out.isEmpty() ? List.of() : List.copyOf(out);
    }
}