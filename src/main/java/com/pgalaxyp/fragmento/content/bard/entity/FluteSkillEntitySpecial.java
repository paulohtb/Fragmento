package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import com.pgalaxyp.fragmento.content.bard.constants.BardVortexConstants;
import com.pgalaxyp.fragmento.core.controller.movement.TimeboxedPositionMovement;
import com.pgalaxyp.fragmento.core.util.MathUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class FluteSkillEntitySpecial extends TimedSkillEntity<FluteSkillEntitySpecial.Phase> {

    enum Phase {
        SPAWN,
        ORBIT,
        CASTED_MOVE_TO_VORTEX,
        CASTED_HOVER,
        DESPAWN
    }

    private static final int ORBIT_ROTATE_TICKS = 30;
    private static final float ROTATE_DEG_PER_TICK = 6.0F;

    private static final double FOLLOW_MAX_SPEED = 0.75;

    private boolean castHandled;
    private Vec3 vortexPos;

    public FluteSkillEntitySpecial(BardSkillEntityBase spirit) {
        super(spirit);
        castHandled = false;
        startPhase(Phase.SPAWN, 6);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase == Phase.SPAWN) {
            s.setAnimKey(BardAnimKeys.SPAWN);
            s.flightController.setEnabled(false);
            s.flightController.setSnapToDesired(false);

            LivingEntity owner = s.getOwner();
            if (owner != null) {
                Vec3 forward = owner.getLookAngle().normalize();
                Vec3 start = owner.position().add(forward.scale(1.2)).add(0.0, 1.0, 0.0);
                s.setPos(start.x, start.y, start.z);
            }
            return;
        }

        if (phase == Phase.ORBIT) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setMaxSpeedPerTick(1.10);
            s.flightController.setAccelPerTick(0.26);
            s.flightController.setSnapToDesired(false);
            s.flightController.setMovement((self, target) -> {
                if (!(target instanceof Player p)) return Vec3.ZERO;

                Vec3 desiredPos = resolveFixedFront(p);
                Vec3 delta = desiredPos.subtract(self.position());
                return MathUtil.clampLength(delta, FOLLOW_MAX_SPEED);
            });
            s.flightController.setEnabled(true);
            return;
        }

        if (phase == Phase.CASTED_MOVE_TO_VORTEX || phase == Phase.CASTED_HOVER) {
            s.setAnimKey(BardAnimKeys.TRAVEL);

            s.flightController.setMaxSpeedPerTick(1.20);
            s.flightController.setAccelPerTick(0.0);
            s.flightController.setSnapToDesired(true);
            s.flightController.setMovement(
                    new TimeboxedPositionMovement<>(
                            this::remainingTicksForMovement,
                            this::resolveVortexTop,
                            0.90
                    )
            );
            s.flightController.setEnabled(true);
            s.setLookAtPos(vortexPos);
            return;
        }

        if (phase == Phase.DESPAWN) {
            s.setAnimKey(BardAnimKeys.DESPAWN);
            s.flightController.setEnabled(false);
            s.flightController.setSnapToDesired(false);
            s.clearLookAtPos();
        }
    }

    @Override
    protected void onTickPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        if (phase != Phase.DESPAWN && !s.isCasted()) {
            LivingEntity t = s.getTarget();
            if (!(t instanceof Player)) {
                startPhase(Phase.DESPAWN, 6);
                return;
            }
        }

        if (s.isCasted() && !castHandled) {
            vortexPos = s.resolveAnchorPosition();
            startPhase(Phase.CASTED_MOVE_TO_VORTEX, 10);
            castHandled = true;
            return;
        }

        if (phase == Phase.SPAWN) {
            if (time() >= duration()) startPhase(Phase.ORBIT, ORBIT_ROTATE_TICKS);
            return;
        }

        if (phase == Phase.ORBIT) {
            LivingEntity t = s.getTarget();
            if (t instanceof ServerPlayer p) {
                float yaw = Mth.wrapDegrees(p.getYRot() + ROTATE_DEG_PER_TICK);
                p.setYRot(yaw);
                p.yRotO = yaw;
                p.setYHeadRot(yaw);
                p.setYBodyRot(yaw);
            }

            if (time() >= duration()) startPhase(Phase.ORBIT, ORBIT_ROTATE_TICKS);
            return;
        }

        if (phase == Phase.CASTED_MOVE_TO_VORTEX) {
            if (time() >= duration()) startPhase(Phase.CASTED_HOVER, BardVortexConstants.MEDIUM_LIFETIME_TICKS);
            return;
        }

        if (phase == Phase.CASTED_HOVER) {
            if (time() >= duration()) startPhase(Phase.DESPAWN, 6);
            return;
        }

        if (phase == Phase.DESPAWN) {
            if (time() >= duration()) s.discard();
        }
    }

    @Override
    protected void onCasted() {
        spawnEffect(spirit());
    }

    private void spawnEffect(BardSkillEntityBase spirit) {
        if (!(spirit.level() instanceof ServerLevel level)) return;

        LivingEntity owner = spirit.getOwner();
        if (owner == null) return;

        var ownerId = owner.getUUID();
        if (!WindVortexLimitService.tryReserve(level, ownerId, WindVortexLimitService.VortexTier.MEDIUM)) return;

        Vec3 c = spirit.resolveAnchorPosition();

        MediumWindVortex vortex = new MediumWindVortex(
                com.pgalaxyp.fragmento.content.bard.registry.VortexHelperRegistry.MEDIUM_WIND_VORTEX.get(),
                level
        );

        vortex.setOwner(owner);
        vortex.markReservedCount();
        vortex.setPos(c.x, c.y, c.z);

        if (!level.addFreshEntity(vortex)) {
            WindVortexLimitService.release(level, ownerId, WindVortexLimitService.VortexTier.MEDIUM);
            return;
        }

        spirit.setLookAtPos(c);
    }

    @Override
    protected void onCancelled() {
        startPhase(Phase.DESPAWN, 6);
    }

    private int remainingTicksForMovement() {
        int t = time();
        int d = duration();
        int rem = t <= 0 ? d : (d - t + 1);
        if (rem <= 0) rem = 1;
        return rem;
    }

    private Vec3 resolveVortexTop() {
        Vec3 base = vortexPos != null ? vortexPos : spirit().position();
        return new Vec3(base.x, base.y + 2.5, base.z);
    }

    private static Vec3 resolveFixedFront(Player p) {
        Vec3 forward = p.getLookAngle().normalize();
        return p.position().add(forward.scale(1.2)).add(0.0, 1.0, 0.0);
    }
}
