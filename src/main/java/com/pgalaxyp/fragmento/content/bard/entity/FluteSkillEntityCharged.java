package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.FlightController;
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
        spirit.bounceController.setEnabled(false);

        spirit.setOrientationLocked(false);

        vortexSpawned = false;

        startPhase(Phase.SPAWN, 8);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        switch (phase) {
            case SPAWN -> {
                s.setAnimKey(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.SPAWN);
                s.flightController.setEnabled(false);
                s.collisionController.setEnabled(false);
                s.bounceController.setEnabled(false);
                s.setOrientationLocked(false);
                s.setDeltaMovement(Vec3.ZERO);
            }
            case TRAVEL -> {
                s.setAnimKey(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.TRAVEL);
                s.setOrientationLocked(false);

                s.flightController.setMovement(
                        new TimedHomingMovement<>(
                                duration(),
                                0,
                                (self, target) -> target.getBoundingBox().getCenter()
                        )
                );
                s.flightController.setEnabled(true);

                s.collisionController.setCollisionCheck(
                        s.collisionController.sweptDetectOnly(0.2)
                );
                s.collisionController.setScanIntervalTicks(2);
                s.collisionController.setScanOtherEntities(true);
                s.collisionController.setEnabled(true);
            }
            case OVERSHOOT -> {
                s.setAnimKey(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.TRAVEL);
                s.setOrientationLocked(false);

                LivingEntity t = s.getTarget();
                Vec3 dir;

                if (t != null) {
                    Vec3 d = t.getBoundingBox().getCenter().subtract(s.position());
                    dir = d.lengthSqr() < 1.0E-8 ? new Vec3(0, 0, 1) : d.normalize();
                } else {
                    dir = new Vec3(0, 0, 1);
                }

                overshootDir = dir;
                overshootVelocity.setDir(overshootDir);

                s.flightController.setMovement(new TimedLinearMovement<>(duration(), overshootVelocity));
                s.flightController.setEnabled(true);

                s.collisionController.setEnabled(false);
            }
            case ASCENT -> {
                s.setAnimKey(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.TRAVEL);
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
            }
            case HOVER -> {
                s.setAnimKey(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.TRAVEL);

                LivingEntity t = s.getTarget();
                if (t == null) {
                    startPhase(Phase.DESPAWN, 8);
                    return;
                }

                Vec3 current = s.position();
                Vec3 base = t.position();
                hoverOffset = current.subtract(base);

                followOffset.setOffset(hoverOffset);

                vortexSpawned = false;

                s.flightController.setMovement(followOffset);
                s.flightController.setEnabled(true);

                s.setOrientationLocked(true);
            }
            case DESPAWN -> {
                s.setAnimKey(com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys.DESPAWN);
                s.setOrientationLocked(false);
                s.flightController.setEnabled(false);
                s.collisionController.setEnabled(false);
                s.bounceController.setEnabled(false);
                s.setDeltaMovement(Vec3.ZERO);
            }
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
            case SPAWN -> {
                if (time() >= duration()) startPhase(Phase.TRAVEL, 8);
            }
            case TRAVEL -> {
                if (s.collisionController.hasCollision()) {
                    LivingEntity hit = s.collisionController.getCollisionTarget();
                    s.collisionController.resetCollision();

                    if (hit != null && hit.isAlive()) onHit(hit);

                    startPhase(Phase.OVERSHOOT, 2);
                    return;
                }

                if (time() >= duration()) startPhase(Phase.OVERSHOOT, 2);
            }
            case OVERSHOOT -> {
                if (time() >= duration()) startPhase(Phase.ASCENT, 5);
            }
            case ASCENT -> {
                if (time() >= duration()) startPhase(Phase.HOVER, 10);
            }
            case HOVER -> {
                if (!vortexSpawned && time() >= 5) {
                    spawnVortex();
                    vortexSpawned = true;
                }
                if (time() >= duration()) startPhase(Phase.DESPAWN, 8);
            }
            case DESPAWN -> {
                if (time() >= duration()) s.discard();
            }
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

    private static final class FollowOffsetMovement implements FlightController.Movement<BardSkillEntityBase> {
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
