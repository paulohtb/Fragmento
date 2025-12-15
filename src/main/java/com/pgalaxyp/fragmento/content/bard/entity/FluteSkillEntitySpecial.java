package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardAnimKeys;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public final class FluteSkillEntitySpecial
        extends TimedSkillEntity<FluteSkillEntitySpecial.Phase> {

    enum Phase {
        SPAWN,
        ORBIT,
        CASTED_MOVE_TO_VORTEX,
        CASTED_HOVER,
        DESPAWN
    }

    private boolean castHandled;
    private Vec3 vortexPos;

    private double orbitAngle;

    public FluteSkillEntitySpecial(BardSkillEntityBase spirit) {
        super(spirit);
        castHandled = false;
        orbitAngle = 0.0;
        startPhase(Phase.SPAWN, 6);
    }

    @Override
    protected void onEnterPhase(Phase phase) {
        BardSkillEntityBase s = spirit();

        switch (phase) {
            case SPAWN -> {
                s.setAnimKey(BardAnimKeys.SPAWN);
                s.flightController.setEnabled(false);

                LivingEntity owner = s.getOwner();
                if (owner != null) {
                    Vec3 forward = owner.getLookAngle().normalize();
                    Vec3 start = owner.position().add(forward.scale(1.2)).add(0.0, 1.0, 0.0);
                    s.setPos(start.x, start.y, start.z);
                }
            }

            case ORBIT -> {
                orbitAngle = 0.0;
                s.flightController.setMovement(
                        (self, target, age) -> {
                            if (!(target instanceof Player p)) return self.position();
                            orbitAngle += 0.15;
                            double r = 2.5;
                            return new Vec3(
                                    p.getX() + r * Math.cos(orbitAngle),
                                    p.getY() + 1.5,
                                    p.getZ() + r * Math.sin(orbitAngle)
                            );
                        }
                );
                s.flightController.setEnabled(true);
            }

            case CASTED_MOVE_TO_VORTEX, CASTED_HOVER -> {
                s.flightController.setMovement(
                        (self, target, age) -> resolveVortexTop()
                );
                s.flightController.setEnabled(true);
                s.setLookAtPos(vortexPos);
            }

            case DESPAWN -> {
                s.setAnimKey(BardAnimKeys.DESPAWN);
                s.flightController.setEnabled(false);
                s.clearLookAtPos();
            }
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

        switch (phase) {
            case SPAWN, ORBIT -> {
                if (time() >= duration()) startPhase(Phase.ORBIT, 30);
            }

            case CASTED_MOVE_TO_VORTEX -> {
                if (time() >= duration()) startPhase(Phase.CASTED_HOVER, 80);
            }

            case CASTED_HOVER -> {
                if (time() >= duration()) startPhase(Phase.DESPAWN, 6);
            }

            case DESPAWN -> {
                if (time() >= duration()) s.discard();
            }
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

        UUID ownerId = owner.getUUID();
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

    private Vec3 resolveVortexTop() {
        Vec3 base = vortexPos != null ? vortexPos : spirit().position();
        return new Vec3(base.x, base.y + 2.5, base.z);
    }
}
