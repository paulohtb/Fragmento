package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.behavior.TimedSpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class FluteSpecialBehavior extends TimedSpiritBehavior<FluteSpecialBehavior.Phase> {

    enum Phase { SPAWN, ORBIT, ASCENT, HOVER, DESPAWN }

    private static final int SPAWN_TICKS = 5;

    private static final int ORBIT_TICKS = 35;
    private static final double ORBIT_SPEED = 0.35;

    private static final int ASCENT_TICKS = 5;
    private static final int HOVER_TICKS = 90;
    private static final int DESPAWN_TICKS = 5;

    private static final int AOE_PULSES = 2;
    private static final int AOE_EDGE_OFFSET_TICKS = 5;
    private static final int AOE_EFFECT_TICKS = 20;

    private static final double POS_EPS_SQR = 1.0E-8;

    private Vec3 castAnchor;
    private Vec3 hoverPos;
    private boolean targetCleared;

    private boolean orbitInit;
    private double orbitStartAngle;
    private double orbitRadius;
    private double orbitYOffset;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        startPhase(Phase.SPAWN, SPAWN_TICKS);
    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
    }

    @Override
    public void onCasted(SpiritContext ctx) {
        if (castAnchor == null) {
            castAnchor = ctx.pos;
        }
        clearTargetOnce(ctx);
        if (phase() == Phase.SPAWN || phase() == Phase.ORBIT) {
            startPhase(Phase.ASCENT, ASCENT_TICKS);
        }
    }

    @Override
    protected void tickInternal(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        Phase p = phase();

        if (p == Phase.SPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.SPAWN);
            if (time() >= duration()) {
                orbitInit = false;
                startPhase(Phase.ORBIT, ORBIT_TICKS);
            }
            return;
        }

        if (p == Phase.ORBIT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            if (ctx.casted) {
                if (castAnchor == null) {
                    castAnchor = ctx.pos;
                }
                clearTargetOnce(ctx);
                startPhase(Phase.ASCENT, ASCENT_TICKS);
                return;
            }

            LivingEntity target = ctx.target;
            if (target == null || !target.isAlive()) {
                ctx.self.requestDespawn();
                return;
            }

            Vec3 center = target.getBoundingBox().getCenter();

            if (!orbitInit) {
                double dx = ctx.pos.x - center.x;
                double dz = ctx.pos.z - center.z;
                double r = Math.sqrt(dx * dx + dz * dz);

                orbitRadius = Math.max(0.5, r);
                orbitStartAngle = Math.atan2(dz, dx);
                orbitYOffset = ctx.pos.y - center.y;

                orbitInit = true;
            }

            int t = time() - 1;
            if (t < 0) t = 0;
            if (t > ORBIT_TICKS - 1) t = ORBIT_TICKS - 1;

            int denom = Math.max(1, ORBIT_TICKS - 1);
            double progress = (double) t / (double) denom;
            double angle = orbitStartAngle + (progress * 6.283185307179586);

            Vec3 orbitPos = center.add(
                    Math.cos(angle) * orbitRadius,
                    orbitYOffset,
                    Math.sin(angle) * orbitRadius
            );

            Vec3 delta = orbitPos.subtract(ctx.pos);

            movement.kind = MovementPlan.Kind.VELOCITY;
            movement.desiredVelocity = delta.scale(ORBIT_SPEED);

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = center;

            return;
        }

        if (p == Phase.ASCENT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            clearTargetOnce(ctx);

            Vec3 anchor = castAnchor != null ? castAnchor : ctx.pos;
            Vec3 targetPos = anchor.add(0.0, 2.0, 0.0);

            if (ctx.self instanceof Entity ent) {
                Vec3 cur = ent.position();
                Vec3 to = targetPos.subtract(cur);

                double dist = to.length();
                if (dist > 1.0E-12) {
                    int remainingSteps = Math.max(1, duration() - time() + 1);
                    double maxStep = 0.75;
                    double desiredStep = dist / (double) remainingSteps;
                    double step = Math.min(maxStep, desiredStep);

                    movement.kind = MovementPlan.Kind.VELOCITY;
                    movement.desiredVelocity = to.scale(step / dist);

                    look.kind = LookPlan.Kind.TO_POS;
                    look.lookAtPos = targetPos;
                } else {
                    movement.kind = MovementPlan.Kind.NONE;
                    look.kind = LookPlan.Kind.NONE;
                }
            }

            if (time() >= duration()) {
                startPhase(Phase.HOVER, HOVER_TICKS);
            }
            return;
        }

        if (p == Phase.HOVER) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            clearTargetOnce(ctx);

            if (ctx.self instanceof Entity ent) {
                if (hoverPos == null) {
                    hoverPos = ent.position();
                }

                Vec3 cur = ent.position();
                if (cur.distanceToSqr(hoverPos) > POS_EPS_SQR) {
                    ent.setPos(hoverPos.x, hoverPos.y, hoverPos.z);
                }

                if (ent.level() instanceof ServerLevel level) {
                    int idx = time() - 1;
                    if (shouldPulse(idx, HOVER_TICKS, AOE_PULSES, AOE_EDGE_OFFSET_TICKS)) {
                        applyGroundGlow(level, hoverPos);
                    }
                }
            }

            movement.kind = MovementPlan.Kind.NONE;
            look.kind = LookPlan.Kind.NONE;

            if (time() >= duration()) {
                startPhase(Phase.DESPAWN, DESPAWN_TICKS);
            }
            return;
        }

        ctx.self.setAnimKey(BardAnimKeys.DESPAWN);
        if (time() >= duration()) {
            ctx.self.requestDespawn();
        }
    }

    private void clearTargetOnce(SpiritContext ctx) {
        if (targetCleared) return;
        if (ctx.self instanceof NewwSpiritEntityBase base) {
            base.clearTarget();
            targetCleared = true;
        }
    }

    private static boolean shouldPulse(int idx, int duration, int pulses, int edgeOffset) {
        if (idx < 0 || idx >= duration) return false;
        int p = Math.max(1, pulses);
        int d = Math.max(1, duration);
        int off = Math.max(0, edgeOffset);

        for (int i = 1; i <= p; i++) {
            int t = (d * i) / p;
            t = t - off;
            int pulseIdx = t - 1;
            if (pulseIdx == idx) {
                return true;
            }
        }

        return false;
    }

    private static void applyGroundGlow(ServerLevel level, Vec3 pos) {
        int x = Mth.floor(pos.x);
        int z = Mth.floor(pos.z);

        int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        int groundY = top - 1;

        AABB area = new AABB(
                x - 1,
                groundY,
                z - 1,
                x + 2,
                groundY + 3,
                z + 2
        );

        List<ServerPlayer> players = level.getEntitiesOfClass(ServerPlayer.class, area);
        if (players.isEmpty()) return;

        for (ServerPlayer p : players) {
            p.addEffect(new MobEffectInstance(MobEffects.GLOWING, AOE_EFFECT_TICKS, 0, false, true, true));
        }
    }

    @Override
    protected void onTickPhase(Phase phase, int time, int duration) {
    }

    @Override
    protected void onEnterPhase(Phase phase) {
    }
}