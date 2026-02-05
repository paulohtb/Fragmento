package com.pgalaxyp.fragmento.combat.damageModule.minecraft;

import com.pgalaxyp.fragmento.combat.util.HealthUnits;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import com.pgalaxyp.fragmento.combat.damageModule.port.DamageWorldCommandPort;
import com.pgalaxyp.fragmento.combat.platformModule.minecraft.McLivingEntityLookup;
import java.util.Objects;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;

public final class McDamageWorldCommandPort implements DamageWorldCommandPort {
    private final MinecraftServer server;

    public McDamageWorldCommandPort(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override public void apply(DamageApplied damage) {
        Objects.requireNonNull(damage);
        LivingEntity target = McLivingEntityLookup.findLiving(server, damage.targetActorId().value());
        if (target == null) return;
        LivingEntity attacker = McLivingEntityLookup.findLiving(server, damage.sourceActorId().value());
        target.hurt(source(target, attacker, damage.spec()), HealthUnits.healthPointsFromHearts(damage.damageHearts()));
    }

    private static DamageSource source(LivingEntity target, LivingEntity attacker, DamageSpec spec) {
        return switch (spec.type()) {case MAGIC -> attacker == null ? target.damageSources().magic() : target.damageSources().indirectMagic(attacker, attacker);
        };
    }
}