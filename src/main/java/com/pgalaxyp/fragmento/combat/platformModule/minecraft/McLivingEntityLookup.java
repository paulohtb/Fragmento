package com.pgalaxyp.fragmento.combat.platformModule.minecraft;

import java.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class McLivingEntityLookup {
    public static LivingEntity findLiving(MinecraftServer server, UUID uuid) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(uuid);
        Entity p = server.getPlayerList().getPlayer(uuid);
        if (p instanceof LivingEntity le) return le;
        for (ServerLevel level : server.getAllLevels()) {
            Entity e = level.getEntity(uuid);
            if (e instanceof LivingEntity le) return le;
        }
        return null;
    }

    private McLivingEntityLookup() {}
}