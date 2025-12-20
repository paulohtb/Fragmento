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

public final class FluteSpecialBehavior extends TimedSpiritBehavior<FluteSpecialBehavior.Phase> {

    enum Phase { SPAWN, ORBIT, ASCENT, HOVER, DESPAWN }

    private static final int SPAWN_TICKS = 5;
    private static final int ORBIT_TICKS = 35;
    private static final double ORBIT_SPEED = 0.45;
    private static final int ASCENT_TICKS = 5;
    private static final int HOVER_TICKS = 90;
    private static final int DESPAWN_TICKS = 5;

    private static final int AOE_PULSES = 2;
    private static final int AOE_EDGE_OFFSET_TICKS = 5;
    private static final int AOE_EFFECT_TICKS = 20;

    private static final int VORTEX_LOOP_DURATION = 40;
    private static final int VORTEX_GAP_DURATION = 5;
    private static final int VORTEX_LOOPS = 2;

    private static final double POS_EPS_SQR = 1.0E-8;

    private boolean orbitInit;
    private double orbitStartAngle;
    private double orbitRadius;
    private double orbitYOffset;

    private boolean targetCleared;
    private boolean vortexSent;

    private Vec3 lastTargetFootPos;
    private Vec3 ascentTargetPos;
    private Vec3 hoverPos;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
        vortexSent = false;
        targetCleared = false;
        orbitInit = false;
        lastTargetFootPos = null;
        ascentTargetPos = null;
        hoverPos = null;
        startPhase(ctx, Phase.SPAWN, SPAWN_TICKS);
    }

    @Override
    public void onImpact(SpiritContext ctx, ImpactResult impact) {
    }

    @Override
    public void onCasted(SpiritContext ctx) {
        if (phase() == Phase.ORBIT) {
            captureLastTargetPosition(ctx);
            clearTargetOnce(ctx);
            startPhase(ctx, Phase.ASCENT, ASCENT_TICKS);
        }
    }

    @Override
    protected void tickInternal(SpiritContext ctx, MovementPlan movement, LookPlan look) {
        Phase p = phase();

        if (p == Phase.SPAWN) {
            ctx.self.setAnimKey(BardAnimKeys.SPAWN);
            if (time() >= duration()) {
                startPhase(ctx, Phase.ORBIT, ORBIT_TICKS);
            }
            return;
        }

        if (p == Phase.ORBIT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            LivingEntity target = ctx.target;
            if (target == null || !target.isAlive()) {
                ctx.self.requestDespawn();
                return;
            }

            Vec3 center = target.getBoundingBox().getCenter();

            if (!orbitInit) {
                Vec3 rel = ctx.pos.subtract(center);
                orbitRadius = Math.max(0.5, Math.sqrt(rel.x * rel.x + rel.z * rel.z));
                orbitStartAngle = Math.atan2(rel.z, rel.x);
                orbitYOffset = ctx.pos.y - center.y;
                orbitInit = true;
            }

            int t = Mth.clamp(time() - 1, 0, ORBIT_TICKS - 1);
            int denom = Math.max(1, ORBIT_TICKS - 1);
            double progress = (double) t / (double) denom;

            double angle = orbitStartAngle + progress * Mth.TWO_PI;

            Vec3 orbitPos = new Vec3(
                    center.x + Math.cos(angle) * orbitRadius,
                    center.y + orbitYOffset,
                    center.z + Math.sin(angle) * orbitRadius
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

            if (ctx.self instanceof Entity ent) {
                if (ascentTargetPos == null) {
                    Vec3 cur = ent.position();
                    Vec3 base = lastTargetFootPos;
                    Vec3 dir = new Vec3(base.x - cur.x, 0.0, base.z - cur.z);
                    double len = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
                    if (len > 1.0E-6) dir = dir.scale(1.0 / len);
                    Vec3 horizontalOffset = dir.scale(-0.5);
                    double y = lastTargetFootPos.y + getTargetHeight(ctx) + 1.0;
                    ascentTargetPos = new Vec3(
                            base.x + horizontalOffset.x,
                            y,
                            base.z + horizontalOffset.z
                    );
                }

                Vec3 cur = ent.position();
                Vec3 to = ascentTargetPos.subtract(cur);
                double dist = to.length();

                if (dist > 1.0E-6) {
                    double rem = (double) duration() + 1.0 - time();
                    int steps = Math.max(1, (int) rem);
                    double step = Math.min(0.75, dist / (double) steps);

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

            if (time() >= duration()) {
                startPhase(ctx, Phase.HOVER, HOVER_TICKS);
            }
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

                look.kind = LookPlan.Kind.TO_POS;
                look.lookAtPos = lastTargetFootPos;

                if (ent.level() instanceof ServerLevel level) {
                    int idx = time() - 1;
                    if (shouldPulse(idx, HOVER_TICKS, AOE_PULSES, AOE_EDGE_OFFSET_TICKS)) {
                        applyGroundGlow(level, lastTargetFootPos);
                    }
                }
            }

            movement.kind = MovementPlan.Kind.NONE;

            if (time() >= duration()) {
                startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            }
            return;
        }

        ctx.self.setAnimKey(BardAnimKeys.DESPAWN);
        if (time() >= duration()) {
            ctx.self.requestDespawn();
        }
    }

    @Override
    protected void onEnterPhase(SpiritContext ctx, Phase phase, int duration) {
        if (!(ctx.self instanceof NewwSpiritEntityBase base)) return;

        int life = base.getLifetime();

        if (phase == Phase.ORBIT || phase == Phase.HOVER) {
            base.setVisualState(NewwSpiritEntityBase.VISUAL_HOVER, life, duration);

            if (phase == Phase.HOVER && !vortexSent && base.level() instanceof ServerLevel level) {
                vortexSent = true;
                Vec3 p = lastTargetFootPos;
                ChunkPos chunkPos = new ChunkPos(Mth.floor(p.x) >> 4, Mth.floor(p.z) >> 4);
                PacketDistributor.sendToPlayersTrackingChunk(
                        level,
                        chunkPos,
                        new MinorWindVortexVisualPacket(
                                p,
                                VORTEX_LOOP_DURATION,
                                VORTEX_GAP_DURATION,
                                VORTEX_LOOPS
                        )
                );
            }
            return;
        }

        if (phase == Phase.DESPAWN) {
            base.setVisualState(NewwSpiritEntityBase.VISUAL_BURST, life, duration);
            return;
        }

        base.setVisualState(NewwSpiritEntityBase.VISUAL_NONE, life, 1);
    }

    @Override
    protected void onTickPhase(SpiritContext ctx, Phase phase, int time, int duration) {
    }

    private void captureLastTargetPosition(SpiritContext ctx) {
        LivingEntity target = ctx.target;
        if (target != null) lastTargetFootPos = target.position();
    }

    private double getTargetHeight(SpiritContext ctx) {
        LivingEntity target = ctx.target;
        return target == null ? 0.0 : target.getBbHeight();
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
        for (int i = 1; i <= Math.max(1, pulses); i++) {
            int t = (duration * i) / pulses - edgeOffset - 1;
            if (t == idx) return true;
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
}