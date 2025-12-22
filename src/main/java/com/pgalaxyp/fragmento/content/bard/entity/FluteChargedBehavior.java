package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.network.s2c.MinorWindVortexVisualPacket;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.behavior.TimedSpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public final class FluteChargedBehavior extends TimedSpiritBehavior<FluteChargedBehavior.Phase> {

    enum Phase { SPAWN, TRAVEL, ASCENT, HOVER, DESPAWN }

    private static final int SPAWN_TICKS = 8;
    private static final int TRAVEL_TICKS = 10;
    private static final int ASCENT_TICKS = 4;
    private static final int HOVER_TICKS = 40;
    private static final int DESPAWN_TICKS = 8;

    private static final int PULSE_OFFSET_TICKS = 5;
    private static final int EFFECT_TICKS = 20;

    private static final int VORTEX_LOOP_DURATION = 40;
    private static final int VORTEX_GAP_DURATION = 0;
    private static final int VORTEX_LOOPS = 1;
    private static final float VORTEX_SIZE_XZ = 3.0F;

    private static final double POS_EPS_SQR = 0.00000001;

    private static final double SUCTION_RADIUS = 3.0;
    private static final double SUCTION_MAX_PULL = 0.06;
    private static final double KNOCK_UP_VELOCITY = Double.longBitsToDouble(0x3FEB333333333333L);

    private static final double NEG_ONE = Double.longBitsToDouble(0xBFF0000000000000L);

    private Vec3 lastTargetFootPos;
    private Vec3 travelDir;
    private Vec3 ascentTargetPos;
    private Vec3 hoverPos;

    private boolean vortexSent;
    private boolean pulseApplied;

    private int suctionTicksLeft;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        lastTargetFootPos = null;
        travelDir = null;
        ascentTargetPos = null;
        hoverPos = null;
        vortexSent = false;
        pulseApplied = false;
        suctionTicksLeft = 0;
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
            if (target == null || !target.isAlive()) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }

            if (!com.pgalaxyp.fragmento.system.entity.movement.SpiritMovementPatterns.chaseLivingTarget(
                    ctx,
                    movement,
                    look,
                    target,
                    time(),
                    duration(),
                    0.0,
                    false
            )) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }

            if (movement.kind == MovementPlan.Kind.VELOCITY) {
                Vec3 v = movement.desiredVelocity;
                double ls = v.lengthSqr();
                if (ls > 0.0000000001) {
                    double inv = 1.0 / Math.sqrt(ls);
                    travelDir = new Vec3(v.x * inv, 0.0, v.z * inv);
                }
            }

            if (time() >= duration()) startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            return;
        }

        if (p == Phase.ASCENT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            LivingEntity target = ctx.target;
            if (target == null || !target.isAlive()) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
                return;
            }

            if (ctx.self instanceof Entity ent) {
                if (ascentTargetPos == null) {
                    if (lastTargetFootPos == null) lastTargetFootPos = target.position();

                    Vec3 dir = travelDir;
                    if (dir == null || dir.lengthSqr() <= 0.0000000001) {
                        Vec3 to = lastTargetFootPos.subtract(ent.position());
                        double ls = to.x * to.x + to.z * to.z;
                        if (ls > 0.0000000001) {
                            double inv = 1.0 / Math.sqrt(ls);
                            dir = new Vec3(to.x * inv, 0.0, to.z * inv);
                        } else {
                            dir = new Vec3(0.0, 0.0, 1.0);
                        }
                    }

                    Vec3 back = dir.scale(NEG_ONE);
                    double y = lastTargetFootPos.y + target.getBbHeight() + 1.0;

                    ascentTargetPos = new Vec3(
                            ent.position().x + back.x,
                            y,
                            ent.position().z + back.z
                    );
                }

                Vec3 to = ascentTargetPos.subtract(ent.position());
                double dist = to.length();

                if (dist > 0.000001) {
                    double rem = (double) duration() + 1.0 + neg((double) time());
                    int steps = Math.max(1, (int) rem);
                    double step = Math.min(0.85, dist / (double) steps);

                    movement.kind = MovementPlan.Kind.VELOCITY;
                    movement.desiredVelocity = to.scale(step / dist);

                    look.kind = LookPlan.Kind.TO_POS;
                    look.lookAtPos = lastTargetFootPos;
                } else {
                    movement.kind = MovementPlan.Kind.NONE;
                    look.kind = LookPlan.Kind.TO_POS;
                    look.lookAtPos = lastTargetFootPos;
                }
            }

            if (time() >= duration()) startPhase(ctx, Phase.HOVER, HOVER_TICKS);
            return;
        }

        if (p == Phase.HOVER) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            if (ctx.self instanceof Entity ent) {
                if (hoverPos == null) hoverPos = ent.position();

                Vec3 cur = ent.position();
                if (cur.distanceToSqr(hoverPos) > POS_EPS_SQR) {
                    ent.setPos(hoverPos.x, hoverPos.y, hoverPos.z);
                }

                movement.kind = MovementPlan.Kind.NONE;

                look.kind = lastTargetFootPos != null ? LookPlan.Kind.TO_POS : LookPlan.Kind.NONE;
                look.lookAtPos = lastTargetFootPos;

                if (ent.level() instanceof ServerLevel level) {
                    int pulseTime = subInt(HOVER_TICKS, PULSE_OFFSET_TICKS);
                    if (!pulseApplied && time() == pulseTime) {
                        applyPulseKnockUp(level, lastTargetFootPos, ctx.owner);
                        suctionTicksLeft = EFFECT_TICKS;
                        pulseApplied = true;
                    }

                    if (suctionTicksLeft > 0 && lastTargetFootPos != null) {
                        applySuctionTick(level, lastTargetFootPos, ctx.owner);
                        suctionTicksLeft = subInt(suctionTicksLeft, 1);
                    }
                }
            }

            if (time() >= duration() && suctionTicksLeft <= 0) startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            return;
        }

        ctx.self.setAnimKey(BardAnimKeys.DESPAWN);

        if (ctx.self instanceof Entity ent && ent.level() instanceof ServerLevel level) {
            if (suctionTicksLeft > 0 && lastTargetFootPos != null) {
                applySuctionTick(level, lastTargetFootPos, ctx.owner);
                suctionTicksLeft = subInt(suctionTicksLeft, 1);
            }
        }

        if (time() >= duration() && suctionTicksLeft <= 0) ctx.self.requestDespawn();
    }

    @Override
    protected void onEnterPhase(SpiritContext ctx, Phase phase, int duration) {
        if (phase != Phase.HOVER) return;
        if (!(ctx.self instanceof NewwSpiritEntityBase base)) return;
        if (!(base.level() instanceof ServerLevel level)) return;

        LivingEntity target = ctx.target;
        if (target == null || !target.isAlive()) return;

        lastTargetFootPos = target.position();

        ChunkPos chunkPos = new ChunkPos(
                Mth.floor(lastTargetFootPos.x) >>> 4,
                Mth.floor(lastTargetFootPos.z) >>> 4
        );

        PacketDistributor.sendToPlayersTrackingChunk(
                level,
                chunkPos,
                new MinorWindVortexVisualPacket(
                        lastTargetFootPos,
                        VORTEX_LOOP_DURATION,
                        VORTEX_GAP_DURATION,
                        VORTEX_LOOPS,
                        VORTEX_SIZE_XZ
                )
        );

        vortexSent = true;
    }

    @Override
    protected void onTickPhase(SpiritContext ctx, Phase phase, int time, int duration) {
    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
        if (phase() != Phase.TRAVEL) return;

        LivingEntity target = ctx.target;
        if (target == null || !target.isAlive()) {
            startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            return;
        }

        lastTargetFootPos = target.position();
        ascentTargetPos = null;
        hoverPos = null;
        vortexSent = false;
        pulseApplied = false;
        suctionTicksLeft = 0;

        startPhase(ctx, Phase.ASCENT, ASCENT_TICKS);
    }

    private static void applyPulseKnockUp(ServerLevel level, Vec3 center, LivingEntity owner) {
        if (center == null) return;

        AABB area = new AABB(
                center.x + neg(SUCTION_RADIUS),
                center.y + neg(1.0),
                center.z + neg(SUCTION_RADIUS),
                center.x + SUCTION_RADIUS,
                center.y + 3.0,
                center.z + SUCTION_RADIUS
        );

        List<LivingEntity> mobs = level.getEntitiesOfClass(LivingEntity.class, area);
        if (mobs.isEmpty()) return;

        for (LivingEntity e : mobs) {
            if (owner != null && e.getId() == owner.getId()) continue;

            Vec3 dm = e.getDeltaMovement();
            double y = Math.max(dm.y, KNOCK_UP_VELOCITY);
            e.setDeltaMovement(dm.x, y, dm.z);
        }
    }

    private static void applySuctionTick(ServerLevel level, Vec3 center, LivingEntity owner) {
        AABB area = new AABB(
                center.x + neg(SUCTION_RADIUS),
                center.y + neg(1.0),
                center.z + neg(SUCTION_RADIUS),
                center.x + SUCTION_RADIUS,
                center.y + 3.0,
                center.z + SUCTION_RADIUS
        );

        List<LivingEntity> mobs = level.getEntitiesOfClass(LivingEntity.class, area);
        if (mobs.isEmpty()) return;

        for (LivingEntity e : mobs) {
            if (owner != null && e.getId() == owner.getId()) continue;

            Vec3 p = e.position();
            double dx = center.x + neg(p.x);
            double dz = center.z + neg(p.z);

            double distSqr = dx * dx + dz * dz;
            if (distSqr <= 0.0001) continue;

            double dist = Math.sqrt(distSqr);
            if (dist > SUCTION_RADIUS) continue;

            double t = 1.0 + neg(dist / SUCTION_RADIUS);
            double pull = Math.min(SUCTION_MAX_PULL, SUCTION_MAX_PULL * t);

            double nx = dx / dist;
            double nz = dz / dist;

            Vec3 dm = e.getDeltaMovement();
            e.setDeltaMovement(
                    dm.x + nx * pull,
                    dm.y,
                    dm.z + nz * pull
            );
        }
    }

    private static int subInt(int a, int b) {
        return a + (~b + 1);
    }

    private static double neg(double v) {
        return Double.longBitsToDouble(Double.doubleToRawLongBits(v) ^ 0x8000000000000000L);
    }
}
