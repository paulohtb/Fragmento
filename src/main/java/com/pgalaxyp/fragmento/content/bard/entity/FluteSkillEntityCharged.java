package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.AutoMovementController;
import com.pgalaxyp.fragmento.core.controller.CollisionController;
import com.pgalaxyp.fragmento.core.controller.movement.ConstantSpeedHomingMovement;
import com.pgalaxyp.fragmento.core.util.MathUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class FluteSkillEntityCharged extends TimedSkillEntity<FluteSkillEntityCharged.Phase> {

    enum Phase { SPAWN, TRAVEL, OVERSHOOT, ASCENT, HOVER, DESPAWN }

    private static final int TRAVEL_TICKS = 18;
    private static final double TRAVEL_MAX_SPEED = 2.25;

    private static final int OVERSHOOT_TICKS = 10;
    private static final double OVERSHOOT_SPEED = 0.35;

    private static final int ASCENT_TICKS = 16;
    private static final double ASCENT_SPEED = 0.16;

    private static final int HOVER_TICKS = 80;
    private static final double HOVER_DRIFT_SPEED = 0.05;

    private static final double BOUNCE_IMPULSE = 0.28;
    private static final double BOUNCE_UP = 0.06;

    private Vec3 moveDir = null;

    public FluteSkillEntityCharged(BardSkillEntityBase spirit) {
        super(spirit);
        startPhase(Phase.SPAWN, 10);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase == Phase.SPAWN) {
            s.setAnimKey(BardAnimKeys.SPAWN);
            s.flightController.setEnabled(false);
            s.flightController.setSnapToDesired(false);
            s.collisionController.setEnabled(false);
            return;
        }

        if (phase == Phase.TRAVEL) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setMaxSpeedPerTick(TRAVEL_MAX_SPEED);
            s.flightController.setAccelPerTick(0.0);
            s.flightController.setSnapToDesired(true);
            s.flightController.setMovement(
                    new ConstantSpeedHomingMovement<>(TRAVEL_MAX_SPEED)
            );
            s.flightController.setEnabled(true);

            s.collisionController.setCollisionCheck(
                    CollisionController.adaptiveHomingHit(
                            s,
                            0.02,
                            0.0,
                            0.0
                    )
            );

            s.collisionController.resetCollision();
            s.collisionController.setEnabled(true);
            return;
        }

        if (phase == Phase.OVERSHOOT) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setSnapToDesired(true);
            s.flightController.setMaxSpeedPerTick(OVERSHOOT_SPEED);
            s.flightController.setAccelPerTick(0.0);

            final Vec3 dir = moveDir == null ? new Vec3(0.0, 0.0, 1.0) : moveDir;

            s.flightController.setMovement(new AutoMovementController.Movement<>() {
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

            s.flightController.setMovement(new AutoMovementController.Movement<>() {
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

            s.flightController.setMovement(new AutoMovementController.Movement<>() {
                @Override
                public Vec3 desiredVelocity(BardSkillEntityBase self, LivingEntity target) {
                    if (target == null) return Vec3.ZERO;

                    Vec3 desiredPos = target.getBoundingBox().getCenter().add(0.0, 2.25, 0.0);
                    Vec3 delta = desiredPos.subtract(self.position());
                    double d = delta.length();
                    if (d <= 0.00000001) return Vec3.ZERO;

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
            s.flightController.setSnapToDesired(false);
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
            if (s.getTarget() == null) {
                startPhase(Phase.DESPAWN, 12);
                return;
            }

            if (s.collisionController.hasAnyCollision()) {
                LivingEntity hit = s.collisionController.getCollisionTarget();
                if (hit != null) {
                    hit.hurt(hit.damageSources().magic(), BardInstrumentConstants.CHARGED_DAMAGE);
                }

                captureMoveDirFromCollisionOrDelta(s);
                applyBounceNow(s);

                startPhase(Phase.OVERSHOOT, OVERSHOOT_TICKS);
                return;
            }

            if (time() >= duration()) {
                startPhase(Phase.DESPAWN, 12);
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
        int t = time();
        int d = duration();
        int rem = t <= 0 ? d : (d + negateInt(t) + 1);
        if (rem <= 0) rem = 1;
        return rem;
    }

    private void captureMoveDirFromCollisionOrDelta(BardSkillEntityBase s) {
        Vec3 dir = s.collisionController.getCollisionMotionDir();
        if (dir != null && dir.lengthSqr() > 0.00000001) {
            moveDir = dir.normalize();
            return;
        }

        Vec3 v = s.getDeltaMovement();
        if (v.lengthSqr() > 0.00000001) {
            moveDir = v.normalize();
            return;
        }

        LivingEntity t = s.getTarget();
        if (t != null) {
            Vec3 d = s.position().subtract(t.getBoundingBox().getCenter());
            moveDir = d.lengthSqr() > 0.00000001 ? d.normalize() : new Vec3(0.0, 0.0, 1.0);
        } else {
            moveDir = new Vec3(0.0, 0.0, 1.0);
        }
    }

    private static void applyBounceNow(BardSkillEntityBase s) {
        Vec3 dir = s.collisionController.getCollisionMotionDir();

        if (dir == null) {
            Vec3 v = s.getDeltaMovement();
            if (v.lengthSqr() > 0.00000001) {
                dir = v.normalize();
            } else {
                LivingEntity t = s.getTarget();
                if (t != null) {
                    Vec3 d = s.position().subtract(t.getBoundingBox().getCenter());
                    dir = d.lengthSqr() > 0.00000001 ? d.normalize() : new Vec3(0.0, 0.0, 1.0);
                } else {
                    dir = new Vec3(0.0, 0.0, 1.0);
                }
            }
        }

        Vec3 back = dir.scale(MathUtil.negate(BOUNCE_IMPULSE)).add(0.0, BOUNCE_UP, 0.0);
        s.setDeltaMovement(back);
    }

    private static int negateInt(int v) {
        return (int) MathUtil.negate((double) v);
    }
}