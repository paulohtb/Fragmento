package com.pgalaxyp.fragmento.combat.old.system.entity.behavior;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class ImpactResult {

    private final LivingEntity entity;
    private final Vec3 pos;

    private ImpactResult(LivingEntity entity, Vec3 pos) {
        this.entity = entity;
        this.pos = pos;
    }

    public static ImpactResult entity(LivingEntity e, Vec3 pos) {
        return new ImpactResult(e, pos);
    }

    public LivingEntity hitEntity() {
        return entity;
    }

    public Vec3 impactPos() {
        return pos;
    }
}