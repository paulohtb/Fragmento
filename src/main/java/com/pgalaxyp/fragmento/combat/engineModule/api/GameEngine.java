package com.pgalaxyp.fragmento.combat.engineModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import com.pgalaxyp.fragmento.combat.flowModule.api.FlowPipeline;
import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.commandModule.api.CommandSourcePort;
import java.util.*;

public final class GameEngine {
    private final CommandSourcePort commands;
    private final FlowPipeline pipeline;
    private final WorldCommandPort world;
    private long nextFrame;

    public GameEngine(CommandSourcePort commands, FlowPipeline pipeline, WorldCommandPort world, long initialFrameId) {
        this.commands = Objects.requireNonNull(commands);
        this.pipeline = Objects.requireNonNull(pipeline);
        this.world = Objects.requireNonNull(world);
        if (initialFrameId < 0) throw new IllegalArgumentException();
        this.nextFrame = initialFrameId + 1L;
    }

    public void step(int tickIndex) {
        var frame = new FrameContext(nextFrame++, tickIndex);
        var bus = pipeline.run(frame, commands.drain(), Map.of());
        world.apply(frame, bus.events());
    }
}