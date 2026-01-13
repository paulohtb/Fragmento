package com.pgalaxyp.fragmento.rpg.targeting.minecraft;

import com.pgalaxyp.fragmento.rpg.targeting.api.TargetingService;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.WorldRaycastAccess;
import com.pgalaxyp.fragmento.rpg.targeting.system.DefaultTargetingService;
import net.minecraft.server.MinecraftServer;

public final class NeoForgeTargetingBootstrap {

    public record ServerModule(
            TargetingService service,
            WorldRaycastAccess world
    ) {
        public ServerModule {
            if (service == null || world == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public static ServerModule createServer(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }
        WorldRaycastAccess world = new NeoForgeWorldRaycast(server);
        TargetingService service = new DefaultTargetingService();
        return new ServerModule(service, world);
    }

    private NeoForgeTargetingBootstrap() {}
}