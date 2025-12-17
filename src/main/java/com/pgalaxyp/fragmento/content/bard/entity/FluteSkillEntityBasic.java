package com.pgalaxyp.fragmento.content.bard.entity;

import java.util.UUID;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardChargeData;
import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.core.controller.movement.TimeboxedHomingToOffsetMovement;
import com.pgalaxyp.fragmento.core.util.MathUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public final class FluteSkillEntityBasic extends TimedSkillEntity<FluteSkillEntityBasic.Phase> {

    enum Phase { SPAWN, TRAVEL, DESPAWN }

    private static final double BOUNCE_IMPULSE = 0.28;
    private static final double BOUNCE_UP = 0.06;

    private static final double STOP_EPSILON = 0.06;

    public FluteSkillEntityBasic(BardSkillEntityBase spirit) {
        super(spirit);
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
                startPhase(Phase.TRAVEL, 10);
            }
            return;
        }

        if (phase == Phase.TRAVEL) {
            LivingEntity target = s.getTarget();
            if (target == null) {
                startPhase(Phase.DESPAWN, 8);
                return;
            }

            if (time() >= duration()) {
                applyImpactAndHit(s, target);
                applyBounceNow(s, target);
                startPhase(Phase.DESPAWN, 8);
                return;
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

        target.hurt(target.damageSources().magic(), BardInstrumentConstants.BASIC_DAMAGE);

        if (s.level() instanceof ServerLevel && s.getOwner() instanceof ServerPlayer sp) {
            UUID src = s.getSourceInstrumentUuid();
            ItemStack inst = BardCatalystIdService.findInPlayerInventory(sp, src);
            if (!inst.isEmpty()) {
                BardChargeData.increment(inst);
            }
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