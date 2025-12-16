package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardVortexConstants;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class MinorWindVortex extends WindVortexBase {

    private static final int PULSE_TICK = 20;

    private int suckTicks;

    public MinorWindVortex(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected WindVortexLimitService.VortexTier tier() {
        return WindVortexLimitService.VortexTier.MINOR;
    }

    @Override
    protected int lifetimeLimitTicks() {
        return BardVortexConstants.MINOR_LIFETIME_TICKS;
    }

    @Override
    protected double radius() {
        return BardVortexConstants.MINOR_RADIUS;
    }

    @Override
    protected double pullStrength() {
        return suckTicks < PULSE_TICK ? BardVortexConstants.MINOR_PULL_STRENGTH : 0.0;
    }

    @Override
    protected int scanIntervalTicks() {
        return BardVortexConstants.MINOR_SCAN_INTERVAL_TICKS;
    }

    @Override
    protected int maxAffectedPerScan() {
        return BardVortexConstants.MINOR_MAX_AFFECTED_PER_SCAN;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            suckTicks++;
        }
    }

    @Override
    protected void applyExtraEffect(LivingEntity e) {
        if (suckTicks != PULSE_TICK) return;

        Vec3 d = e.position().subtract(position());
        if (d.lengthSqr() < 1.0E-8) return;

        Vec3 out = d.normalize().scale(0.60);
        Vec3 up = new Vec3(0.0, 0.55, 0.0);

        e.setDeltaMovement(e.getDeltaMovement().add(out).add(up));
        e.hurtMarked = true;
    }

    @Override
    protected void applyPlayerBuff(Player p) {
    }
}
