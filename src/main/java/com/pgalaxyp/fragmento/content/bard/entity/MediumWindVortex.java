package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardVortexConstants;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class MediumWindVortex extends WindVortexBase {

    private static final int PULSE_1_TICK = 39;
    private static final int PULSE_2_TICK = 80;

    public MediumWindVortex(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected WindVortexLimitService.VortexTier tier() {
        return WindVortexLimitService.VortexTier.MEDIUM;
    }

    @Override
    protected int lifetimeLimitTicks() {
        return BardVortexConstants.MEDIUM_LIFETIME_TICKS;
    }

    @Override
    protected double radius() {
        return BardVortexConstants.MEDIUM_RADIUS;
    }

    @Override
    protected double pullStrength() {
        return BardVortexConstants.MEDIUM_PULL_STRENGTH;
    }

    @Override
    protected int scanIntervalTicks() {
        return BardVortexConstants.MEDIUM_SCAN_INTERVAL_TICKS;
    }

    @Override
    protected int maxAffectedPerScan() {
        return BardVortexConstants.MEDIUM_MAX_AFFECTED_PER_SCAN;
    }

    @Override
    protected void applyPlayerBuff(Player p) {
        var cur = p.getEffect(MobEffects.GLOWING);
        if (cur == null || cur.getDuration() <= 12) {
            p.addEffect(new MobEffectInstance(MobEffects.GLOWING, BardVortexConstants.MEDIUM_GLOWING_TICKS));
        }
    }

    @Override
    protected void applyExtraEffect(LivingEntity e) {
        int t = lifetimeTicks();
        if (t != PULSE_1_TICK && t != PULSE_2_TICK) return;

        Vec3 center = position();
        Vec3 fromCenter = e.position().subtract(center);
        if (fromCenter.lengthSqr() < 1.0E-8) return;

        Vec3 up = new Vec3(0.0, 0.55, 0.0);

        Vec3 impulse;
        if (t == PULSE_1_TICK) {
            Vec3 in = fromCenter.normalize().scale(-0.25);
            impulse = in.add(up);
        } else {
            Vec3 out = fromCenter.normalize().scale(0.55);
            impulse = out.add(new Vec3(0.0, 0.65, 0.0));
        }

        e.setDeltaMovement(e.getDeltaMovement().add(impulse));
        e.hurtMarked = true;
    }
}
