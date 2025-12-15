package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class FluteSkillEntitySpecial extends TimedSkillEntity<FluteSkillEntitySpecial.Phase> {

    enum Phase {
        SPAWN,
        ORBIT,
        CASTED_MOVE_TO_VORTEX,
        CASTED_HOVER,
        DESPAWN
    }

    private boolean castHandled;

    private Vec3 vortexPos;

    private final OrbitMovement orbitMovement = new OrbitMovement();
    private final FixedGoalMovement fixedGoalMovement = new FixedGoalMovement();

    public FluteSkillEntitySpecial(BardSkillEntityBase spirit) {
        super(spirit);
        castHandled = false;
        spirit.setOrientationLocked(false);
        startPhase(Phase.SPAWN, 6);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        switch (phase) {
            case SPAWN -> {
                s.setAnimKey(BardAnimKeys.SPAWN);
                s.flightController.setEnabled(false);

                LivingEntity owner = s.getOwner();
                if (owner != null) {
                    Vec3 forward = owner.getLookAngle();
                    if (forward.lengthSqr() < 1.0E-8) forward = new Vec3(0.0, 0.0, 1.0);
                    forward = forward.normalize();

                    Vec3 start = owner.position().add(forward.scale(1.2)).add(0.0, 1.0, 0.0);
                    s.moveTo(start.x, start.y, start.z);
                }
            }
            case ORBIT -> {
                orbitMovement.reset();
                s.flightController.setMovement(orbitMovement);
                s.flightController.setEnabled(true);
                s.setOrientationLocked(false);
            }
            case CASTED_MOVE_TO_VORTEX -> {
                s.setTarget(null);
                s.setOrientationLocked(false);

                Vec3 top = resolveVortexTop();
                fixedGoalMovement.setGoal(top);

                s.flightController.setMovement(fixedGoalMovement);
                s.flightController.setEnabled(true);

                s.setLookAtPos(vortexPos);
            }
            case CASTED_HOVER -> {
                s.setTarget(null);
                s.setOrientationLocked(true);

                Vec3 top = resolveVortexTop();
                fixedGoalMovement.setGoal(top);

                s.flightController.setMovement(fixedGoalMovement);
                s.flightController.setEnabled(true);

                s.setLookAtPos(vortexPos);
            }
            case DESPAWN -> {
                s.setAnimKey(BardAnimKeys.DESPAWN);
                s.flightController.setEnabled(false);
                s.setOrientationLocked(false);
                s.clearLookAtPos();
                s.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    @Override
    protected void onTickPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase != Phase.DESPAWN && !s.isCasted()) {
            LivingEntity t = s.getTarget();
            if (!(t instanceof Player)) {
                startPhase(Phase.DESPAWN, 6);
                return;
            }
        }

        if (s.isCasted() && !castHandled) {
            vortexPos = s.resolveAnchorPosition();
            startPhase(Phase.CASTED_MOVE_TO_VORTEX, 10);
            castHandled = true;
            return;
        }

        switch (phase) {
            case SPAWN, ORBIT -> {
                if (time() >= duration()) startPhase(Phase.ORBIT, 30);
            }
            case CASTED_MOVE_TO_VORTEX -> {
                if (time() >= duration()) startPhase(Phase.CASTED_HOVER, 80);
            }
            case CASTED_HOVER -> {
                if (time() >= duration()) startPhase(Phase.DESPAWN, 6);
            }
            case DESPAWN -> {
                if (time() >= duration()) s.discard();
            }
        }
    }

    @Override
    protected void onCasted() {
        spawnEffect(spirit());
    }

    private void spawnEffect(BardSkillEntityBase spirit) {
        if (!(spirit.level() instanceof ServerLevel level)) return;

        LivingEntity owner = spirit.getOwner();
        if (owner == null) return;

        UUID ownerId = owner.getUUID();
        if (!WindVortexLimitService.tryReserve(level, ownerId, WindVortexLimitService.VortexTier.MEDIUM)) return;

        Vec3 c = spirit.resolveAnchorPosition();

        MediumWindVortex vortex = new MediumWindVortex(
                com.pgalaxyp.fragmento.content.bard.registry.VortexHelperRegistry.MEDIUM_WIND_VORTEX.get(),
                level
        );

        vortex.setOwner(owner);
        vortex.markReservedCount();
        vortex.setPos(c.x, c.y, c.z);

        if (!level.addFreshEntity(vortex)) {
            WindVortexLimitService.release(level, ownerId, WindVortexLimitService.VortexTier.MEDIUM);
            return;
        }

        spirit.setLookAtPos(c);
    }

    @Override
    protected void onCancelled() {
        startPhase(Phase.DESPAWN, 6);
    }

    private Vec3 resolveVortexTop() {
        Vec3 base = vortexPos != null ? vortexPos : spirit().position();
        return new Vec3(base.x, base.y + 2.5, base.z);
    }

    private static final class OrbitMovement implements AutoMovementController.Movement<BardSkillEntityBase> {
        private double angle;

        void reset() {
            angle = 0.0;
        }

        @Override
        public void apply(BardSkillEntityBase self, LivingEntity target, int age) {
            if (self == null) return;
            if (!(target instanceof Player p)) return;

            angle += 0.15;
            if (angle > Math.PI * 2) angle -= Math.PI * 2;

            double radius = 2.5;
            Vec3 goal = new Vec3(
                    p.getX() + radius * Math.cos(angle),
                    p.getY() + 1.5,
                    p.getZ() + radius * Math.sin(angle)
            );

            self.setDeltaMovement(goal.subtract(self.position()).scale(0.25));
        }
    }

    private static final class FixedGoalMovement implements AutoMovementController.Movement<BardSkillEntityBase> {
        private Vec3 goal = Vec3.ZERO;

        void setGoal(Vec3 goal) {
            this.goal = goal != null ? goal : Vec3.ZERO;
        }

        @Override
        public void apply(BardSkillEntityBase self, LivingEntity target, int age) {
            if (self == null) return;

            Vec3 delta = goal.subtract(self.position());
            if (delta.lengthSqr() < 1.0E-8) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }

            self.setDeltaMovement(delta.scale(0.25));
        }
    }
}
