package com.pgalaxyp.fragmento.core.controller.movement;

import com.pgalaxyp.fragmento.gameplay.entity.SkillEntityBase;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class ConstantSpeedHomingMovement<T extends SkillEntityBase>
        implements AutoMovementController.Movement<T> {

    private final double speedPerTick;

    public ConstantSpeedHomingMovement(double speedPerTick) {
        this.speedPerTick = Math.max(0.001, speedPerTick);
    }

    @Override
    public Vec3 compute(T self, LivingEntity target) {
        if (target == null || !target.isAlive()) {
            return self.position();
        }

        Vec3 cur = self.position();
        Vec3 goal = target.getBoundingBox().getCenter();
        Vec3 delta = goal.subtract(cur);

        double dist = delta.length();
        if (dist <= speedPerTick) {
            return goal;
        }

        Vec3 step = delta.normalize().scale(speedPerTick);
        return cur.add(step);
    }
}
