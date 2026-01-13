package com.pgalaxyp.fragmento.rpg.targeting.minecraft;

import com.pgalaxyp.fragmento.rpg.targeting.api.*;
import com.pgalaxyp.fragmento.rpg.targeting.bridge.*;
import com.pgalaxyp.fragmento.rpg.targeting.system.*;
import net.minecraft.server.*;

public final class GameTargetingBootstrap {

    public record ServerModule(TargetingService service, WorldRaycastAccess world) {
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

        WorldRaycastAccess world = new GameWorldRaycast(server);
        TargetingService service = new DefaultTargetingService();

        return new ServerModule(service, world);
    }

    private GameTargetingBootstrap() {}
}