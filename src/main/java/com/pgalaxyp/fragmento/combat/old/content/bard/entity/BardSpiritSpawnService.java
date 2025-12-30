package com.pgalaxyp.fragmento.combat.old.content.bard.entity;

import com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Direction;

public final class BardSpiritSpawnService {

    private static final double NORMAL_FORWARD = 1.0;
    private static final double SPECIAL_FORWARD = 1.5;

    private static final double BASE_Y_FACTOR = 0.6;

    private BardSpiritSpawnService() {
    }

    public static Vec3 resolve(ServerLevel level, LivingEntity owner, SkillMode mode) {
        if (level == null || owner == null || mode == null) return null;

        if (mode == SkillMode.SPECIAL) {
            return resolveSpecial(owner);
        }

        return resolveNormal(level, owner);
    }

    private static Vec3 resolveNormal(ServerLevel level, LivingEntity owner) {
        Vec3 base = owner.position().add(0.0, owner.getBbHeight() * BASE_Y_FACTOR, 0.0);
        Vec3 forward = horizontalForward(owner);
        Vec3 left = new Vec3(forward.z, 0.0, -forward.x);
        Vec3 right = new Vec3(-forward.z, 0.0, forward.x);

        boolean pickLeft = level.random.nextBoolean();
        Vec3 side = pickLeft ? left : right;

        return base.add(forward.scale(NORMAL_FORWARD)).add(side.scale(0.6));
    }

    private static Vec3 resolveSpecial(LivingEntity owner) {
        Vec3 base = owner.position().add(0.0, owner.getBbHeight() * BASE_Y_FACTOR, 0.0);
        Vec3 forward = horizontalForward(owner);
        return base.add(forward.scale(SPECIAL_FORWARD));
    }

    private static Vec3 horizontalForward(LivingEntity owner) {
        Vec3 look = owner.getLookAngle();
        Vec3 h = new Vec3(look.x, 0.0, look.z);
        double ls = h.lengthSqr();
        if (ls > 0.0000000001) {
            double inv = 1.0 / Math.sqrt(ls);
            return new Vec3(h.x * inv, 0.0, h.z * inv);
        }

        Direction d = Direction.fromYRot(owner.getYRot());
        Vec3 dir = Vec3.atLowerCornerOf(d.getNormal());
        Vec3 hh = new Vec3(dir.x, 0.0, dir.z);
        double ls2 = hh.lengthSqr();
        if (ls2 > 0.0000000001) {
            double inv2 = 1.0 / Math.sqrt(ls2);
            return new Vec3(hh.x * inv2, 0.0, hh.z * inv2);
        }

        return new Vec3(0.0, 0.0, 1.0);
    }
}