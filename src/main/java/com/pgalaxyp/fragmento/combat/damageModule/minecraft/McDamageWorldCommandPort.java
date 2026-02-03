package com.pgalaxyp.fragmento.combat.damageModule.minecraft;

import com.pgalaxyp.fragmento.combat.util.HealthUnits;
import com.pgalaxyp.fragmento.combat.minecraft.McLivingEntityLookup;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import java.util.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;

public final class McDamageWorldCommandPort {
    private final MinecraftServer server;

    public McDamageWorldCommandPort(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    public void apply(DamageApplied damage) {
        Objects.requireNonNull(damage);
        LivingEntity target = McLivingEntityLookup.findLiving(server, damage.targetActorId().value());
        if (target == null) return;
        float amount = HealthUnits.healthPointsFromHearts(damage.damageHearts());
        target.setHealth(Math.max(0.0f, target.getHealth() - amount));
    }
}