package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.system.entity.behavior.ImpactResult;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.behavior.TimedSpiritBehavior;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.system.entity.movement.MovementPlan;
import java.util.List;
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

public final class FluteSpecialBehavior extends TimedSpiritBehavior<FluteSpecialBehavior.Phase> {

    enum Phase { SPAWN, ORBIT, ASCENT, HOVER, DESPAWN }

    private static final int SPAWN_TICKS = 5;
    private static final int ORBIT_TICKS = 35;
    private static final int ASCENT_TICKS = 5;
    private static final int HOVER_TICKS = 90;
    private static final int DESPAWN_TICKS = 5;

    private static final int AOE_PULSES = 2;
    private static final int AOE_EDGE_OFFSET_TICKS = 5;
    private static final int AOE_EFFECT_TICKS = 40;

    private static final double POS_EPS_SQR = 0.00000001;

    private static final double ORBIT_RADIUS = 1.5;

    private boolean orbitInit;
    private double orbitStartAngle;
    private double orbitYOffset;

    private boolean targetCleared;

    private Vec3 lastTargetFootPos;
    private Vec3 ascentTargetPos;
    private Vec3 hoverPos;

    @Override
    protected void startInitialPhase(SpiritContext ctx) {
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
            if (time() >= duration()) startPhase(ctx, Phase.ORBIT, ORBIT_TICKS);
            return;
        }

        if (p == Phase.ORBIT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            LivingEntity target = ctx.target;
            LivingEntity owner = ctx.owner;
            if (target == null || !target.isAlive()) {
                ctx.self.requestDespawn();
                return;
            }
            if (owner == null || !owner.isAlive()) {
                ctx.self.requestDespawn();
                return;
            }

            Vec3 ownerCenter = owner.getBoundingBox().getCenter();

            if (!orbitInit) {
                Vec3 rel = ctx.pos.subtract(ownerCenter);
                orbitStartAngle = Math.atan2(rel.z, rel.x);
                orbitYOffset = ctx.pos.y + neg(ownerCenter.y);
                orbitInit = true;
            }

            int t = Mth.clamp(time() + (~0), 0, ORBIT_TICKS + (~0));
            int denom = Math.max(1, ORBIT_TICKS + (~0));
            double progress = (double) t / (double) denom;

            double angle = orbitStartAngle + progress * Mth.TWO_PI;

            Vec3 orbitPos = new Vec3(
                    ownerCenter.x + Math.cos(angle) * ORBIT_RADIUS,
                    ownerCenter.y + orbitYOffset,
                    ownerCenter.z + Math.sin(angle) * ORBIT_RADIUS
            );

            if (ctx.self instanceof Entity ent) {
                ent.setPos(orbitPos.x, orbitPos.y, orbitPos.z);
            }

            movement.kind = MovementPlan.Kind.NONE;

            look.kind = LookPlan.Kind.TO_POS;
            look.lookAtPos = target.getBoundingBox().getCenter();
            return;
        }

        if (p == Phase.ASCENT) {
            ctx.self.setAnimKey(BardAnimKeys.TRAVEL);

            if (ctx.self instanceof Entity ent) {
                if (ascentTargetPos == null) {
                    Vec3 cur = ent.position();
                    Vec3 base = lastTargetFootPos;
                    Vec3 dir = new Vec3(base.x + neg(cur.x), 0.0, base.z + neg(cur.z));
                    double len = Math.sqrt(dir.x * dir.x + dir.z * dir.z);
                    if (len > 0.000001) dir = dir.scale(1.0 / len);
                    Vec3 horizontalOffset = dir.scale(Double.longBitsToDouble(0xBFE0000000000000L));
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

                if (dist > 0.000001) {
                    double rem = (double) duration() + 1.0 + neg((double) time());
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

                look.kind = LookPlan.Kind.TO_POS;
                look.lookAtPos = lastTargetFootPos;

                if (ent.level() instanceof ServerLevel level) {
                    int idx = time() + (~0);
                    if (shouldPulse(idx, HOVER_TICKS, AOE_PULSES, AOE_EDGE_OFFSET_TICKS)) {
                        applyGroundSpeed(level, lastTargetFootPos);
                    }
                }
            }

            movement.kind = MovementPlan.Kind.NONE;

            if (time() >= duration()) startPhase(ctx, Phase.DESPAWN, DESPAWN_TICKS);
            return;
        }

        ctx.self.setAnimKey(BardAnimKeys.DESPAWN);
        if (time() >= duration()) ctx.self.requestDespawn();
    }

    @Override
    protected void onEnterPhase(SpiritContext ctx, Phase phase, int duration) {
        if (!(ctx.self instanceof NewwSpiritEntityBase base)) return;

        int life = base.getLifetime();

        if (phase == Phase.ORBIT || phase == Phase.HOVER) {
            base.setVisualState(NewwSpiritEntityBase.VISUAL_HOVER, life, duration);
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
            int t = (duration * i) / pulses + negInt(edgeOffset) + (~0);
            if (t == idx) return true;
        }
        return false;
    }

    private static void applyGroundSpeed(ServerLevel level, Vec3 pos) {
        int x = Mth.floor(pos.x);
        int z = Mth.floor(pos.z);

        int top = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        int groundY = top + (~0);

        AABB area = new AABB(
                x + (~0),
                groundY,
                z + (~0),
                x + 2,
                groundY + 3,
                z + 2
        );

        List<ServerPlayer> players = level.getEntitiesOfClass(ServerPlayer.class, area);
        if (players.isEmpty()) return;

        for (ServerPlayer p : players) {
            p.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, AOE_EFFECT_TICKS, 0, false, true, true));
        }
    }

    private static int negInt(int v) {
        return ~v + 1;
    }

    private static double neg(double v) {
        return Double.longBitsToDouble(Double.doubleToRawLongBits(v) ^ 0x8000000000000000L);
    }
}
