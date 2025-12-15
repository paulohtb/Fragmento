package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import com.pgalaxyp.fragmento.core.controller.movement.TimedGoalMovement;
import com.pgalaxyp.fragmento.core.controller.movement.TimedHomingMovement;
import com.pgalaxyp.fragmento.core.controller.movement.TimedLinearMovement;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class FluteSkillEntityCharged extends TimedSkillEntity<FluteSkillEntityCharged.Phase> {

    enum Phase { SPAWN, TRAVEL, OVERSHOOT, ASCENT, HOVER, DESPAWN }

    private static final double EPS = 0.00000001D;

    private boolean vortexSpawned;

    private Vec3 overshootDir = Vec3.ZERO;
    private Vec3 ascentBase = Vec3.ZERO;
    private Vec3 hoverOffset = Vec3.ZERO;

    private final OvershootVelocity overshootVelocity = new OvershootVelocity();
    private final AscentGoal ascentGoal = new AscentGoal();
    private final FollowOffsetMovement followOffset = new FollowOffsetMovement();

    public FluteSkillEntityCharged(BardSkillEntityBase spirit) {
        super(spirit);

        spirit.flightController.setEnabled(false);
        spirit.collisionController.setEnabled(false);

        spirit.setOrientationLocked(false);

        vortexSpawned = false;

        startPhase(Phase.SPAWN, 8);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        switch (phase) {
            case SPAWN:
                s.setAnimKey(BardAnimKeys.SPAWN);
                s.flightController.setEnabled(false);
                s.collisionController.setEnabled(false);
                s.setOrientationLocked(false);
                s.setDeltaMovement(Vec3.ZERO);
                break;

            case TRAVEL:
                s.setAnimKey(BardAnimKeys.TRAVEL);
                s.setOrientationLocked(false);

                s.flightController.setMovement(
                        new TimedHomingMovement<>(
                                duration(),
                                0,
                                new TimedHomingMovement.GoalProvider<BardSkillEntityBase>() {
                                    @Override
                                    public Vec3 goal(BardSkillEntityBase self, LivingEntity target) {
                                        return target.getBoundingBox().getCenter();
                                    }
                                }
                        )
                );
                s.flightController.setEnabled(true);

                s.collisionController.setCollisionCheck(
                        s.collisionController.sweptDetectOnly(0.2)
                );
                s.collisionController.setScanIntervalTicks(1);
                s.collisionController.setScanOtherEntities(false);
                s.collisionController.setEnabled(true);
                break;

            case OVERSHOOT:
                s.setAnimKey(BardAnimKeys.TRAVEL);
                s.setOrientationLocked(false);

                LivingEntity t1 = s.getTarget();
                Vec3 dir1;

                if (t1 != null) {
                    Vec3 d = t1.getBoundingBox().getCenter().subtract(s.position());
                    if (d.lengthSqr() < EPS) {
                        dir1 = new Vec3(0.0, 0.0, 1.0);
                    } else {
                        dir1 = d.normalize();
                    }
                } else {
                    dir1 = new Vec3(0.0, 0.0, 1.0);
                }

                overshootDir = dir1;
                overshootVelocity.setDir(overshootDir);

                s.flightController.setMovement(new TimedLinearMovement<>(duration(), overshootVelocity));
                s.flightController.setEnabled(true);

                s.collisionController.setEnabled(false);
                break;

            case ASCENT:
                s.setAnimKey(BardAnimKeys.TRAVEL);
                s.setOrientationLocked(false);

                ascentBase = s.position();
                ascentGoal.setBase(ascentBase);

                s.flightController.setMovement(
                        new TimedGoalMovement<>(
                                duration(),
                                ascentGoal,
                                0.25
                        )
                );
                s.flightController.setEnabled(true);
                break;

            case HOVER:
                s.setAnimKey(BardAnimKeys.TRAVEL);

                LivingEntity t2 = s.getTarget();
                if (t2 == null) {
                    startPhase(Phase.DESPAWN, 8);
                    return;
                }

                Vec3 current = s.position();
                Vec3 base = t2.position();
                hoverOffset = current.subtract(base);

                followOffset.setOffset(hoverOffset);

                vortexSpawned = false;

                s.flightController.setMovement(followOffset);
                s.flightController.setEnabled(true);

                s.setOrientationLocked(true);
                break;

            case DESPAWN:
                s.setAnimKey(BardAnimKeys.DESPAWN);
                s.setOrientationLocked(false);
                s.flightController.setEnabled(false);
                s.collisionController.setEnabled(false);
                s.setDeltaMovement(Vec3.ZERO);
                break;
        }
    }

    @Override
    protected void onTickPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase != Phase.DESPAWN && s.getTarget() == null) {
            startPhase(Phase.DESPAWN, 8);
            return;
        }

        switch (phase) {
            case SPAWN:
                if (time() >= duration()) startPhase(Phase.TRAVEL, 8);
                break;

            case TRAVEL:
                if (s.collisionController.hasCollision()) {
                    LivingEntity hit = s.collisionController.getCollisionTarget();
                    s.collisionController.resetCollision();

                    if (hit != null && hit.isAlive()) onHit(hit);

                    startPhase(Phase.OVERSHOOT, 2);
                    return;
                }

                if (time() >= duration()) startPhase(Phase.OVERSHOOT, 2);
                break;

            case OVERSHOOT:
                if (time() >= duration()) startPhase(Phase.ASCENT, 5);
                break;

            case ASCENT:
                if (time() >= duration()) startPhase(Phase.HOVER, 10);
                break;

            case HOVER:
                if (!vortexSpawned && time() >= 5) {
                    spawnVortex();
                    vortexSpawned = true;
                }
                if (time() >= duration()) startPhase(Phase.DESPAWN, 8);
                break;

            case DESPAWN:
                if (time() >= duration()) s.discard();
                break;
        }
    }

    private void spawnVortex() {
        BardSkillEntityBase s = spirit();
        if (!(s.level() instanceof ServerLevel level)) return;

        LivingEntity owner = s.getOwner();
        if (!(owner instanceof ServerPlayer)) return;

        UUID ownerId = owner.getUUID();
        if (!WindVortexLimitService.tryReserve(level, ownerId, WindVortexLimitService.VortexTier.MINOR)) {
            return;
        }

        MinorWindVortex vortex = new MinorWindVortex(
                com.pgalaxyp.fragmento.content.bard.registry.VortexHelperRegistry.WIND_VORTEX.get(),
                level
        );

        vortex.setOwner(owner);
        vortex.markReservedCount();

        LivingEntity t = s.getTarget();
        Vec3 p = t != null ? t.position() : s.position();
        vortex.setPos(p.x, p.y, p.z);

        boolean added = level.addFreshEntity(vortex);
        if (!added) {
            WindVortexLimitService.release(level, ownerId, WindVortexLimitService.VortexTier.MINOR);
        }
    }

    private void onHit(LivingEntity target) {
        target.hurt(target.damageSources().magic(), BardInstrumentConstants.CHARGED_DAMAGE);
    }

    @Override
    protected void onCancelled() {
        startPhase(Phase.DESPAWN, 8);
    }

    private static final class OvershootVelocity implements TimedLinearMovement.VelocityProvider {
        private Vec3 dir = Vec3.ZERO;

        void setDir(Vec3 dir) {
            this.dir = dir != null ? dir : Vec3.ZERO;
        }

        @Override
        public Vec3 velocity() {
            return dir;
        }
    }

    private static final class AscentGoal implements TimedGoalMovement.GoalProvider {
        private Vec3 base = Vec3.ZERO;

        void setBase(Vec3 base) {
            this.base = base != null ? base : Vec3.ZERO;
        }

        @Override
        public Vec3 goal() {
            Vec3 b = base;
            return new Vec3(b.x, b.y + 2.0, b.z);
        }
    }

    private static final class FollowOffsetMovement implements AutoMovementController.Movement<BardSkillEntityBase> {
        private Vec3 offset = Vec3.ZERO;

        void setOffset(Vec3 offset) {
            this.offset = offset != null ? offset : Vec3.ZERO;
        }

        @Override
        public void apply(BardSkillEntityBase self, LivingEntity target, int age) {
            if (self == null) return;
            if (target == null) {
                self.setDeltaMovement(Vec3.ZERO);
                return;
            }
            Vec3 goal = target.position().add(offset);
            self.setDeltaMovement(goal.subtract(self.position()).scale(0.25));
        }
    }
}
