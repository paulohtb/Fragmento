package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.network.s2c.MinorWindVortexVisualPacket;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.behavior.TimedSpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import com.pgalaxyp.fragmento.system.entity.movement.SpiritMovementPatterns;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public final class FluteChargedBehavior extends TimedSpiritBehavior<FluteChargedBehavior.Phase> {

    enum Phase { SPAWN, TRAVEL, ASCENT, HOVER, DESPAWN }

    private static final int SPAWN_TICKS = 8;
    private static final int TRAVEL_TICKS = 10;
    private static final int ASCENT_TICKS = 4;
    private static final int HOVER_TICKS = 20;
    private static final int DESPAWN_TICKS = 8;

    private static final int SUCTION_TICKS = 14;
    private static final int KNOCK_UP_TICK = 15;

    private static final double EFFECT_HALF_SIZE = 2.5;
    private static final double EFFECT_Y_MIN_OFFSET = 0.0;
    private static final double EFFECT_Y_MAX_OFFSET = 1.0;

    private static final double BASE_PULL = 0.5;
    private static final double MAX_PULL = 0.75;
    private static final double MAX_HORIZONTAL_SPEED = 0.5;

    private static final double KNOCK_UP_VELOCITY = 0.85;
    private static final double ABOVE_TARGET_Y = 2.0;

    private static final double VISUAL_SEND_RADIUS = 64.0;

    private static final AliveMobPredicate ALIVE_MOB = new AliveMobPredicate();

    private Vec3 vortexCenter;
    private Vec3 hoverPos;
    private int ascentTargetId;
    private boolean knockUpApplied;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        vortexCenter = null;
        hoverPos = null;
        ascentTargetId = Integer.MIN_VALUE;
        knockUpApplied = false;
        startPhase(ctx, Phase.SPAWN, SPAWN_TICKS);
    }

    @Override
    protected void tickInternal(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        Phase p = phase();

        if (p == Phase.SPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.SPAWN);
            if (time() >= duration()) startPhase(ctx, Phase.TRAVEL, TRAVEL_TICKS);
            return;
        }

        if (p == Phase.TRAVEL) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);
            LivingEntity target = ctx.target;
            if (!SpiritMovementPatterns.chaseLivingTarget(ctx, movement, look, target, time(), duration(), 0.0, false)) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }
            if (time() >= duration()) startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            return;
        }

        if (p == Phase.ASCENT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);
            Mob target = readAscentTarget(ctx);
            if (target == null || !target.isAlive()) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }
            Vec3 dest = hoverPos;
            if (dest == null) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }
            int remaining = duration() + neg(time());
            if (remaining <= 0) {
                movement.kind = MovementPlan.Kind.VELOCITY;
                movement.desiredVelocity = Vec3.ZERO;
                startPhase(ctx, Phase.HOVER, HOVER_TICKS);
                return;
            }
            Vec3 cur = readSelfPos(ctx);
            movement.kind = MovementPlan.Kind.VELOCITY;
            movement.desiredVelocity = dest.subtract(cur).scale(1.0 / remaining);
            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = dest;
            return;
        }

        if (p == Phase.HOVER) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);
            Vec3 center = vortexCenter;
            if (center == null) {
                Mob t = readAscentTarget(ctx);
                if (t != null && t.isAlive()) center = t.position();
                if (center == null) {
                    startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                    return;
                }
                vortexCenter = center;
            }
            movement.kind = MovementPlan.Kind.VELOCITY;
            movement.desiredVelocity = Vec3.ZERO;
            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = center;
            int t = time();
            if (t < SUCTION_TICKS) {
                applySuction(ctx, center, t, SUCTION_TICKS);
            } else if (t == KNOCK_UP_TICK && !knockUpApplied) {
                knockUpApplied = true;
                applyKnockUp(ctx, center);
            }
            if (t >= duration()) startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            return;
        }

        ctx.self.setAnimKey(BardAnimKeys.DESPAWN);
        if (time() >= duration()) ctx.self.requestDespawn();
    }

    @Override
    protected void onEnterPhase(SpiritContext ctx, Phase phase, int duration) {
        if (phase == Phase.HOVER) {
            knockUpApplied = false;
            Mob target = readAscentTarget(ctx);
            Vec3 center = target != null && target.isAlive() ? target.position() : readSelfPos(ctx);
            vortexCenter = center;
            sendVortex(ctx, center, duration, 0);
        }
    }

    @Override
    protected void onTickPhase(SpiritContext ctx, Phase phase, int time, int duration) {

    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
        if (phase() != Phase.TRAVEL) return;
        LivingEntity ctxTarget = ctx.target;
        if (!(ctxTarget instanceof Mob target) || !target.isAlive()) {
            startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            return;
        }
        ascentTargetId = target.getId();
        double targetY = target.getBoundingBox().maxY + ABOVE_TARGET_Y;
        Vec3 selfPos = readSelfPos(ctx);
        vortexCenter = null;
        hoverPos = new Vec3(selfPos.x, targetY, selfPos.z);
        knockUpApplied = false;
        startPhase(ctx, Phase.ASCENT, ASCENT_TICKS);
    }

    private void sendVortex(SpiritContext ctx, Vec3 center, int durationTicks, int startAge) {
        if (!(ctx.self instanceof NewwSpiritEntityBase base)) return;
        if (!(base.level() instanceof ServerLevel level)) return;
        PacketDistributor.sendToPlayersNear(
                level,
                null,
                center.x,
                center.y,
                center.z,
                VISUAL_SEND_RADIUS,
                new MinorWindVortexVisualPacket(center, durationTicks, startAge, 1, (float) (EFFECT_HALF_SIZE * 2.0))
        );
    }

    private void applySuction(SpiritContext ctx, Vec3 center, int age, int maxAge) {
        if (!(ctx.self instanceof NewwSpiritEntityBase base)) return;
        if (!(base.level() instanceof ServerLevel level)) return;

        double progress = age / (double) maxAge;
        double pullRange = MAX_PULL + neg(BASE_PULL);
        double strength = BASE_PULL + pullRange * progress * progress;

        AABB box = buildEffectBox(center);
        List<Mob> entities = level.getEntitiesOfClass(Mob.class, box, ALIVE_MOB);

        for (int i = 0, s = entities.size(); i < s; i++) {
            Mob mob = entities.get(i);
            Vec3 delta = center.subtract(mob.position());

            double ax = Math.abs(delta.x);
            double az = Math.abs(delta.z);
            double d = Math.max(ax, az);

            if (d < 1.0E-6) continue;

            double clamped = Math.min(d / EFFECT_HALF_SIZE, 1.0);
            double falloff = 1.0 + neg(clamped);
            if (falloff <= 0.0) continue;

            double dist = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
            if (dist < 1.0E-6) continue;

            double pull = strength * falloff;
            Vec3 add = new Vec3(delta.x / dist * pull, 0.0, delta.z / dist * pull);

            Vec3 v = mob.getDeltaMovement().add(add);
            double h = Math.sqrt(v.x * v.x + v.z * v.z);

            if (h > MAX_HORIZONTAL_SPEED) {
                double k = MAX_HORIZONTAL_SPEED / h;
                v = new Vec3(v.x * k, v.y, v.z * k);
            }

            mob.setDeltaMovement(v);
            mob.hurtMarked = true;
        }
    }

    private void applyKnockUp(SpiritContext ctx, Vec3 center) {
        if (!(ctx.self instanceof NewwSpiritEntityBase base)) return;
        if (!(base.level() instanceof ServerLevel level)) return;

        AABB box = buildEffectBox(center);
        List<Mob> entities = level.getEntitiesOfClass(Mob.class, box, ALIVE_MOB);

        for (int i = 0, s = entities.size(); i < s; i++) {
            Mob mob = entities.get(i);
            Vec3 v = mob.getDeltaMovement();
            if (v.y < KNOCK_UP_VELOCITY) {
                mob.setDeltaMovement(v.x, KNOCK_UP_VELOCITY, v.z);
                mob.hurtMarked = true;
            }
        }
    }

    private static AABB buildEffectBox(Vec3 center) {
        double minX = center.x + neg(EFFECT_HALF_SIZE);
        double maxX = center.x + EFFECT_HALF_SIZE;
        double minY = center.y + EFFECT_Y_MIN_OFFSET;
        double maxY = center.y + EFFECT_Y_MAX_OFFSET;
        double minZ = center.z + neg(EFFECT_HALF_SIZE);
        double maxZ = center.z + EFFECT_HALF_SIZE;
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private Mob readAscentTarget(SpiritContext ctx) {
        if (ascentTargetId == Integer.MIN_VALUE) return null;
        if (!(ctx.self instanceof NewwSpiritEntityBase base)) return null;
        if (!(base.level() instanceof ServerLevel level)) return null;
        Entity e = level.getEntity(ascentTargetId);
        if (e instanceof Mob mob && mob.isAlive()) return mob;
        return null;
    }

    private static Vec3 readSelfPos(SpiritContext ctx) {
        if (ctx.self instanceof NewwSpiritEntityBase base) return base.position();
        return ctx.pos;
    }

    private static int neg(int v) {
        return Math.negateExact(v);
    }

    private static double neg(double v) {
        return Double.longBitsToDouble(Double.doubleToRawLongBits(v) ^ Long.MIN_VALUE);
    }

    private static final class AliveMobPredicate implements Predicate<Mob> {
        @Override
        public boolean test(Mob mob) {
            return mob.isAlive();
        }
    }
}