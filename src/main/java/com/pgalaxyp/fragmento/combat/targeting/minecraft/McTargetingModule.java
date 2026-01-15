package com.pgalaxyp.fragmento.combat.targeting.minecraft;

import com.pgalaxyp.fragmento.combat.targeting.api.*;
import com.pgalaxyp.fragmento.combat.targeting.bridge.*;
import com.pgalaxyp.fragmento.combat.targeting.system.*;
import net.minecraft.server.*;

public final class McTargetingModule {
    public record ServerModule(TargetingWithWorld service, WorldRaycastAccess world) {}

    public static ServerModule createServer(MinecraftServer server) {
        WorldRaycastAccess world = new McWorldRaycast(server);
        DefaultTargetingService service = new DefaultTargetingService();
        service.bindWorld(world);
        return new ServerModule(service, world);
    }

    private McTargetingModule() {}
}