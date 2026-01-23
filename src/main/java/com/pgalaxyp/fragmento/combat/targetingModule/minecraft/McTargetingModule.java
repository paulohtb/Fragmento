package com.pgalaxyp.fragmento.combat.targetingModule.minecraft;

import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingService;
import com.pgalaxyp.fragmento.combat.targetingModule.system.DefaultTargetingService;
import net.minecraft.server.MinecraftServer;

public final class McTargetingModule {
    public static TargetingService createServer(MinecraftServer server) {
        if (server == null) throw new IllegalArgumentException();
        return new DefaultTargetingService(new McTargetingPort(server));
    }
    private McTargetingModule() {}
}