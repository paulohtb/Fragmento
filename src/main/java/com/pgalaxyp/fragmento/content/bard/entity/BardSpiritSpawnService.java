package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.system.skill.SkillMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class BardSpiritSpawnService {

    private static final double HALF_BLOCK = 0.5;

    private static final int NORMAL_FORWARD = 1;
    private static final int SPECIAL_FORWARD = 2;

    private static final int BASE_UPPER_Y_OFFSET = 1;
    private static final int VERTICAL_BLOCKS = 2;

    private BardSpiritSpawnService() {
    }

    public static Vec3 resolve(ServerLevel level, LivingEntity owner, SkillMode mode) {
        if (level == null || owner == null || mode == null) {
            return null;
        }

        if (mode == SkillMode.SPECIAL) {
            return resolveSpecial(level, owner);
        }

        return resolveNormal(level, owner);
    }

    private static Vec3 resolveNormal(ServerLevel level, LivingEntity owner) {
        BlockPos base = owner.blockPosition();
        Direction forward = Direction.fromYRot(owner.getYRot());
        Direction left = forward.getCounterClockWise();
        Direction right = forward.getClockWise();

        BlockPos anchor = base.above(BASE_UPPER_Y_OFFSET).relative(forward, NORMAL_FORWARD);

        BlockPos a = anchor.relative(left, 1);
        BlockPos b = anchor.relative(right, 1);

        AABB boxA = makeColumnAabb(a);
        AABB boxB = makeColumnAabb(b);

        boolean pickA = level.random.nextBoolean();
        AABB box = pickA ? boxA : boxB;

        double x = lerp(box.minX, box.maxX, level.random.nextDouble());
        double y = lerp(box.minY, box.maxY, level.random.nextDouble());
        double z = lerp(box.minZ, box.maxZ, level.random.nextDouble());

        return new Vec3(x, y, z);
    }

    private static Vec3 resolveSpecial(ServerLevel level, LivingEntity owner) {
        BlockPos base = owner.blockPosition();
        Direction forward = Direction.fromYRot(owner.getYRot());

        BlockPos anchor = base.above(BASE_UPPER_Y_OFFSET).relative(forward, SPECIAL_FORWARD);
        AABB box = makeColumnAabb(anchor);

        return new Vec3(
                box.minX + HALF_BLOCK,
                box.minY + (box.getYsize() * 0.5),
                box.minZ + HALF_BLOCK
        );
    }

    private static AABB makeColumnAabb(BlockPos pos) {
        double minX = pos.getX();
        double minY = pos.getY() - HALF_BLOCK;
        double minZ = pos.getZ();

        double maxX = pos.getX() + 1.0;
        double maxY = (pos.getY() + VERTICAL_BLOCKS) - HALF_BLOCK;
        double maxZ = pos.getZ() + 1.0;

        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static double lerp(double a, double b, double t) {
        if (t <= 0.0) return a;
        if (t >= 1.0) return b;
        return a + (b - a) * t;
    }
}