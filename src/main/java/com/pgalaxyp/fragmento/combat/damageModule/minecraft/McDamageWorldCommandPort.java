package com.pgalaxyp.fragmento.combat.damageModule.minecraft;

import com.pgalaxyp.fragmento.combat.util.HealthUnits;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import net.minecraft.world.entity.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import java.util.*;

public final class McDamageWorldCommandPort {
    private final MinecraftServer server;

    public McDamageWorldCommandPort(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    public void apply(DamageApplied damage) {
        Objects.requireNonNull(damage);
        LivingEntity target = findLiving(damage.targetActorId().value());
        if (target == null) return;
        float amount = HealthUnits.healthPointsFromHearts(damage.hearts());
        target.setHealth(Math.max(0.0f, target.getHealth() - amount));
    }

    private LivingEntity findLiving(UUID uuid) {
        Entity player = server.getPlayerList().getPlayer(uuid);
        if (player instanceof LivingEntity le) return le;
        for (ServerLevel level : server.getAllLevels()) {
            Entity e = level.getEntity(uuid);
            if (e instanceof LivingEntity le) return le;
        }
        return null;
    }
}