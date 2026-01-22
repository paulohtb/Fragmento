package com.pgalaxyp.fragmento.combat.targeting.platform;

import com.pgalaxyp.fragmento.combat.targeting.api.TargetingService;
import com.pgalaxyp.fragmento.combat.targeting.system.DefaultTargetingService;
import net.minecraft.server.MinecraftServer;

public final class McTargetingModule {
    public static TargetingService createServer(MinecraftServer server) {
        if (server == null) throw new IllegalArgumentException();
        return new DefaultTargetingService(new McTargetingPort(server));
    }
    private McTargetingModule() {}
}