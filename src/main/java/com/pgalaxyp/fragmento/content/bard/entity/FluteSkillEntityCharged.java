package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import com.pgalaxyp.fragmento.core.controller.movement.TimeboxedHomingToOffsetMovement;
import com.pgalaxyp.fragmento.core.util.MathUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class FluteSkillEntityCharged extends TimedSkillEntity<FluteSkillEntityCharged.Phase> {

    enum Phase { SPAWN, TRAVEL, OVERSHOOT, ASCENT, HOVER, DESPAWN }

    private static final int TRAVEL_TICKS = 18;

    private static final int OVERSHOOT_TICKS = 10;
    private static final double OVERSHOOT_SPEED = 0.35;

    private static final int ASCENT_TICKS = 16;
    private static final double ASCENT_SPEED = 0.16;

    private static final int HOVER_TICKS = 80;
    private static final double HOVER_DRIFT_SPEED = 0.05;

    private static final double BOUNCE_IMPULSE = 0.28;
    private static final double BOUNCE_UP = 0.06;

    private static final double STOP_EPSILON = 0.06;

    private Vec3 moveDir;

    public FluteSkillEntityCharged(BardSkillEntityBase spirit) {
        super(spirit);
        moveDir = null;
        startPhase(Phase.SPAWN, 10);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase == Phase.SPAWN) {
            s.setAnimKey(BardAnimKeys.SPAWN);
            s.flightController.setEnabled(false);
            s.flightController.setSnapToDesired(true);
            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.TRAVEL) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setSnapToDesired(true);
            s.flightController.setMaxSpeedPerTick(512.0);
            s.flightController.setAccelPerTick(0.0);
            s.flightController.setMovement(
                    new TimeboxedHomingToOffsetMovement<>(this::remainingTicksForMovement, STOP_EPSILON)
            );
            s.flightController.setEnabled(true);

            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.OVERSHOOT) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setSnapToDesired(true);
            s.flightController.setMaxSpeedPerTick(OVERSHOOT_SPEED);
            s.flightController.setAccelPerTick(0.0);

            final Vec3 dir = moveDir == null ? new Vec3(0.0, 0.0, 1.0) : moveDir;

            s.flightController.setMovement(new AutoMovementController.Movement<BardSkillEntityBase>() {
                @Override
                public Vec3 desiredVelocity(BardSkillEntityBase self, LivingEntity target) {
                    return dir.scale(OVERSHOOT_SPEED);
                }
            });

            s.flightController.setEnabled(true);

            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.ASCENT) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setSnapToDesired(true);
            s.flightController.setMaxSpeedPerTick(ASCENT_SPEED);
            s.flightController.setAccelPerTick(0.0);

            s.flightController.setMovement(new AutoMovementController.Movement<BardSkillEntityBase>() {
                @Override
                public Vec3 desiredVelocity(BardSkillEntityBase self, LivingEntity target) {
                    return new Vec3(0.0, ASCENT_SPEED, 0.0);
                }
            });

            s.flightController.setEnabled(true);

            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.HOVER) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setSnapToDesired(true);
            s.flightController.setMaxSpeedPerTick(HOVER_DRIFT_SPEED);
            s.flightController.setAccelPerTick(0.0);

            s.flightController.setMovement(new AutoMovementController.Movement<BardSkillEntityBase>() {
                @Override
                public Vec3 desiredVelocity(BardSkillEntityBase self, LivingEntity target) {
                    if (target == null) return Vec3.ZERO;

                    Vec3 desiredPos = target.getBoundingBox().getCenter().add(0.0, 2.25, 0.0);
                    Vec3 delta = desiredPos.subtract(self.position());
                    double d = delta.length();
                    if (d <= 1.0E-12) return Vec3.ZERO;

                    Vec3 dir = delta.scale(1.0 / d);
                    return dir.scale(HOVER_DRIFT_SPEED);
                }
            });

            s.flightController.setEnabled(true);

            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.DESPAWN) {
            s.setAnimKey(BardAnimKeys.DESPAWN);
            s.flightController.setEnabled(false);
            s.flightController.setSnapToDesired(true);
            s.collisionController.setEnabled(false);
        }
    }

    @Override
    protected void onTickPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase == Phase.SPAWN) {
            if (time() >= duration()) {
                startPhase(Phase.TRAVEL, TRAVEL_TICKS);
            }
            return;
        }

        if (phase == Phase.TRAVEL) {
            LivingEntity target = s.getTarget();
            if (target == null) {
                startPhase(Phase.DESPAWN, 12);
                return;
            }

            if (time() >= duration()) {
                applyImpactAndHit(s, target);
                captureMoveDirFromTargetCenter(s, target);
                applyBounceNow(s, target);
                startPhase(Phase.OVERSHOOT, OVERSHOOT_TICKS);
                return;
            }

            return;
        }

        if (phase == Phase.OVERSHOOT) {
            if (time() >= duration()) {
                startPhase(Phase.ASCENT, ASCENT_TICKS);
            }
            return;
        }

        if (phase == Phase.ASCENT) {
            if (time() >= duration()) {
                startPhase(Phase.HOVER, HOVER_TICKS);
            }
            return;
        }

        if (phase == Phase.HOVER) {
            if (s.getTarget() == null) {
                startPhase(Phase.DESPAWN, 12);
                return;
            }

            if (time() >= duration()) {
                startPhase(Phase.DESPAWN, 12);
            }
            return;
        }

        if (phase == Phase.DESPAWN) {
            if (time() >= duration()) {
                s.discard();
            }
        }
    }

    private int remainingTicksForMovement() {
        int d = duration();
        int t = time();
        int rem = d;
        if (t > 0) {
            rem = (d + (int) MathUtil.negate((double) t)) + 1;
        }
        if (rem <= 0) rem = 1;
        return rem;
    }

    private static void applyImpactAndHit(BardSkillEntityBase s, LivingEntity target) {
        if (s.level().isClientSide()) return;

        Vec3 center = target.getBoundingBox().getCenter();

        double halfTarget = 0.5 * Math.max(target.getBbWidth(), target.getBbHeight());
        double halfSelf = 0.5 * Math.max(s.getBbWidth(), s.getBbHeight());
        double stop = halfTarget + halfSelf + STOP_EPSILON;

        Vec3 fromCenter = s.position().subtract(center);
        Vec3 dirOut;
        if (fromCenter.lengthSqr() > 1.0E-12) {
            dirOut = fromCenter.normalize();
        } else {
            dirOut = new Vec3(0.0, 0.0, 1.0);
        }

        Vec3 impact = center.add(dirOut.scale(stop));

        s.setPos(impact.x, impact.y, impact.z);
        s.setDeltaMovement(Vec3.ZERO);

        target.hurt(target.damageSources().magic(), BardInstrumentConstants.CHARGED_DAMAGE);
    }

    private void captureMoveDirFromTargetCenter(BardSkillEntityBase s, LivingEntity target) {
        Vec3 dir = s.position().subtract(target.getBoundingBox().getCenter());
        if (dir.lengthSqr() > 1.0E-12) {
            moveDir = dir.normalize();
        } else {
            moveDir = new Vec3(0.0, 0.0, 1.0);
        }
    }

    private static void applyBounceNow(BardSkillEntityBase s, LivingEntity target) {
        Vec3 dir = s.position().subtract(target.getBoundingBox().getCenter());
        if (dir.lengthSqr() <= 1.0E-12) {
            Vec3 v = s.getDeltaMovement();
            if (v.lengthSqr() > 1.0E-12) dir = v;
            else dir = new Vec3(0.0, 0.0, 1.0);
        }

        Vec3 back = dir.normalize().scale(MathUtil.negate(BOUNCE_IMPULSE)).add(0.0, BOUNCE_UP, 0.0);
        s.setDeltaMovement(back);
    }
}