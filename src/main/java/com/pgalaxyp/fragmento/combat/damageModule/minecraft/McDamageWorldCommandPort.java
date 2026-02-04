package com.pgalaxyp.fragmento.combat.damageModule.minecraft;

import com.pgalaxyp.fragmento.combat.util.HealthUnits;
import com.pgalaxyp.fragmento.combat.damageModule.api.*;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import com.pgalaxyp.fragmento.combat.platformModule.minecraft.McLivingEntityLookup;
import java.util.Objects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;

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
        DamageSource src = source(target, damage.spec());
        target.hurt(src, amount);
    }

    private static DamageSource source(LivingEntity target, DamageSpec spec) {
        return switch (spec.type()) {
            case MAGIC -> target.damageSources().magic();
        };
    }
}