package com.pgalaxyp.fragmento.feature.bard_class.common.spirit.controller;

import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class OrientationController<T extends SpiritBase> extends SpiritController<T> {

    private final TargetController<?> targetCtrl;

    public OrientationController(T spirit, TargetController<?> targetCtrl) {
        super(spirit);
        this.targetCtrl = targetCtrl;
    }

    @Override
    public void tick() {
        if (!targetCtrl.hasValidTarget()) return;

        Vec3 targetPos = targetCtrl.getTarget().getBoundingBox().getCenter();
        lookAt(targetPos);
    }

    private void lookAt(Vec3 targetPos) {
        Vec3 myPos = spirit.position();
        Vec3 dir = targetPos.subtract(myPos);

        if (dir.lengthSqr() < 0.000001D) return;

        double dx = dir.x;
        double dy = dir.y;
        double dz = dir.z;

        double horizontal = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float)(Math.toDegrees(Math.atan2(dx, dz)));
        float targetPitch = (float)(Math.toDegrees(Math.atan2(-dy, horizontal)));

        float maxTurn = 20.0F;

        float newYaw = Mth.approachDegrees(spirit.getYRot(), targetYaw, maxTurn);
        float newPitch = Mth.approachDegrees(spirit.getXRot(), targetPitch, maxTurn);

        spirit.setYRot(newYaw);
        spirit.yRotO = newYaw;

        spirit.setXRot(newPitch);
        spirit.xRotO = newPitch;
    }

    public void faceInstantly(Vec3 targetPos) {
        Vec3 myPos = spirit.position();
        Vec3 dir = targetPos.subtract(myPos);

        if (dir.lengthSqr() < 0.000001D) return;

        double dx = dir.x;
        double dy = dir.y;
        double dz = dir.z;

        double horizontal = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float)(Math.toDegrees(Math.atan2(dx, dz)));
        float pitch = (float)(Math.toDegrees(Math.atan2(-dy, horizontal)));

        spirit.setYRot(yaw);
        spirit.yRotO = yaw;

        spirit.setXRot(pitch);
        spirit.xRotO = pitch;
    }
}
