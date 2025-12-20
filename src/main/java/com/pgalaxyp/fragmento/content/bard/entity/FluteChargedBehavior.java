package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.network.s2c.MinorWindVortexVisualPacket;
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
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

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

    private static final double POS_EPS_SQR = 1.0E-8;

    private Vec3 lastTargetFootPos;
    private Vec3 travelDir;
    private Vec3 ascentTargetPos;
    private Vec3 hoverPos;

    private boolean vortexSent;
    private boolean pulseApplied;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        lastTargetFootPos = null;
        travelDir = null;
        ascentTargetPos = null;
        hoverPos = null;
        vortexSent = false;
        pulseApplied = false;
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
                if (ls > 1.0E-10) {
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
                    if (dir == null || dir.lengthSqr() <= 1.0E-10) {
                        Vec3 to = lastTargetFootPos.subtract(ent.position());
                        double ls = to.x * to.x + to.z * to.z;
                        if (ls > 1.0E-10) {
                            double inv = 1.0 / Math.sqrt(ls);
                            dir = new Vec3(to.x * inv, 0.0, to.z * inv);
                        } else {
                            dir = new Vec3(0.0, 0.0, 1.0);
                        }
                    }

                    Vec3 back = dir.scale(-1.0);
                    double y = lastTargetFootPos.y + target.getBbHeight() + 1.0;

                    ascentTargetPos = new Vec3(
                            ent.position().x + back.x,
                            y,
                            ent.position().z + back.z
                    );
                }

                Vec3 to = ascentTargetPos.subtract(ent.position());
                double dist = to.length();

                if (dist > 1.0E-6) {
                    double rem = (double) duration() + 1.0 - time();
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
                    if (!pulseApplied && time() == HOVER_TICKS - PULSE_OFFSET_TICKS) {
                        applyGroundGlow(level, lastTargetFootPos);
                        pulseApplied = true;
                    }
                }
            }

            if (time() >= duration()) startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            return;
        }

        ctx.self.setAnimKey(BardAnimKeys.DESPAWN);
        if (time() >= duration()) ctx.self.requestDespawn();
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
                Mth.floor(lastTargetFootPos.x) >> 4,
                Mth.floor(lastTargetFootPos.z) >> 4
        );

        PacketDistributor.sendToPlayersTrackingChunk(
                level,
                chunkPos,
                new MinorWindVortexVisualPacket(
                        lastTargetFootPos,
                        VORTEX_LOOP_DURATION,
                        VORTEX_GAP_DURATION,
                        VORTEX_LOOPS
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

        startPhase(ctx, Phase.ASCENT, ASCENT_TICKS);
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

        List<LivingEntity> mobs = level.getEntitiesOfClass(LivingEntity.class, area);
        if (mobs.isEmpty()) return;

        for (LivingEntity m : mobs) {
            m.addEffect(new MobEffectInstance(MobEffects.GLOWING, EFFECT_TICKS, 0, false, true, true));
        }
    }
}