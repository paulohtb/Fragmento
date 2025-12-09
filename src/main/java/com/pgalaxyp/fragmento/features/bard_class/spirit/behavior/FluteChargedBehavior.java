package com.pgalaxyp.fragmento.features.bard_class.spirit.behavior;

import com.pgalaxyp.fragmento.core.util.MathUtil;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentChargeData;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentConstants;
import com.pgalaxyp.fragmento.features.bard_class.registry.entity.VortexHelperRegistry;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.controller.SpiritConstants;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.WindVortex;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FluteChargedBehavior implements CastedSpiritBase.SpiritBehavior {

    private enum Phase { SPAWN, DASH, ASCEND, HOVER, DESPAWN }

    private Phase phase;
    private int time;
    private int duration;

    private Vec3 dashStart;
    private Vec3 dashEnd;

    private Vec3 ascendStart;
    private Vec3 ascendEnd;

    private boolean vortexSpawned;

    @Override
    public void init(CastedSpiritBase spirit) {
        spirit.setAnimKey("spawn_charged");

        phase = Phase.SPAWN;
        time = 0;
        duration = SpiritConstants.IDLE_TICKS;

        spirit.flightController.setEnabled(false);
        spirit.collisionController.setEnabled(true);
        spirit.spiritBounceController.setEnabled(false);
    }

    @Override
    public void tick(CastedSpiritBase spirit) {
        time++;

        LivingEntity target = spirit.getTarget();
        if (target == null) {
            despawnInit(spirit);
            return;
        }

        switch (phase) {
            case SPAWN -> {
                if (time >= duration) startDash(spirit, target);
            }
            case DASH -> tickDash(spirit);
            case ASCEND -> tickAscend(spirit);
            case HOVER -> tickHover(spirit);
            case DESPAWN -> {
                if (time >= duration) spirit.discard();
            }
        }
    }

    private void startDash(CastedSpiritBase spirit, LivingEntity target) {
        phase = Phase.DASH;
        time = 0;
        duration = SpiritConstants.TRAVEL_TICKS + SpiritConstants.CHARGED_OVERSHOOT_TICKS;

        dashStart = spirit.position();
        Vec3 center = target.getBoundingBox().getCenter();
        Vec3 dir = center.subtract(dashStart);

        if (dir.lengthSqr() < 1.0E-4) dir = new Vec3(0, 0, 1);
        else dir = dir.normalize();

        dashEnd = center.add(dir.scale(SpiritConstants.CHARGED_OVERSHOOT_DISTANCE));

        spirit.setAnimKey("dash");
    }

    private void tickDash(CastedSpiritBase spirit) {
        int t = time;
        double f = MathUtil.clamp01((double) t / duration);

        Vec3 current = spirit.position();
        Vec3 desired = MathUtil.lerp(dashStart, dashEnd, f);

        spirit.setDeltaMovement(desired.subtract(current));

        if (t >= duration) startAscend(spirit);
    }

    private void startAscend(CastedSpiritBase spirit) {
        LivingEntity target = spirit.getTarget();
        if (target == null) {
            despawnInit(spirit);
            return;
        }

        phase = Phase.ASCEND;
        time = 0;
        duration = SpiritConstants.CHARGED_RISE_TICKS;

        ascendStart = spirit.position();
        Vec3 center = target.getBoundingBox().getCenter();
        ascendEnd = new Vec3(center.x, center.y + SpiritConstants.CHARGED_RISE_HEIGHT, center.z);

        spirit.setAnimKey("ascend");
    }

    private void tickAscend(CastedSpiritBase spirit) {
        int t = time;
        double f = MathUtil.clamp01((double) t / duration);

        Vec3 current = spirit.position();
        Vec3 desired = MathUtil.lerp(ascendStart, ascendEnd, f);

        spirit.setDeltaMovement(desired.subtract(current));

        if (t >= duration) startHover(spirit);
    }

    private void startHover(CastedSpiritBase spirit) {
        phase = Phase.HOVER;
        time = 0;
        duration = SpiritConstants.CHARGED_HOVER_DURATION;

        vortexSpawned = false;

        spirit.setDeltaMovement(Vec3.ZERO);

        spirit.setAnimKey("hover");
    }

    private void tickHover(CastedSpiritBase spirit) {
        spirit.setDeltaMovement(Vec3.ZERO);

        if (!vortexSpawned && time >= 10) {
            spawnVortex(spirit);
            vortexSpawned = true;
        }

        if (time >= duration) despawnInit(spirit);
    }

    private void spawnVortex(CastedSpiritBase spirit) {
        if (!(spirit.level() instanceof ServerLevel level)) return;

        LivingEntity target = spirit.getTarget();
        if (target == null) return;

        Vec3 c = target.getBoundingBox().getCenter();
        double y = target.getBoundingBox().minY;

        WindVortex vortex = new WindVortex(VortexHelperRegistry.WIND_VORTEX.get(), level);
        vortex.setOwner(spirit.getOwner());
        vortex.setPos(c.x, y, c.z);
        level.addFreshEntity(vortex);

        level.playSound(null, c.x, y, c.z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.7F, 1.0F);
    }

    private void despawnInit(CastedSpiritBase spirit) {
        phase = Phase.DESPAWN;
        time = 0;
        duration = SpiritConstants.CHARGED_DESPAWN_TICKS;

        spirit.flightController.setEnabled(false);
        spirit.collisionController.setEnabled(false);
        spirit.spiritBounceController.setEnabled(false);

        spirit.setDeltaMovement(Vec3.ZERO);

        spirit.setAnimKey("despawn_charged");
    }

    @Override
    public void onHit(CastedSpiritBase spirit, LivingEntity target) {
        target.hurt(target.damageSources().magic(), InstrumentConstants.CHARGED_DAMAGE);

        if (spirit.getOwner() instanceof ServerPlayer player) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof InstrumentBase) {
                InstrumentChargeData.reset(stack);
            }
        }
    }
}
