package com.pgalaxyp.fragmento.features.bard_class.spirit.behavior;

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

public class FluteChargedBehavior extends SpiritBehavior {

    private enum Phase { SPAWN, TRAVEL, OVERSHOOT, ASCENT, HOVER, DESPAWN }

    private Phase phase;
    private int time;
    private double duration;
    private boolean vortexSpawned;
    private Vec3 hoverOffset;

    public FluteChargedBehavior(CastedSpiritBase spirit) {
        super(spirit);
        phase = Phase.SPAWN;
        time = 0;
        duration = 7.5;
        vortexSpawned = false;

        CastedSpiritBase s = spirit();
        s.flightController.setEnabled(false);
        s.collisionController.setEnabled(false);
        s.spiritBounceController.setEnabled(false);
        s.orientationController.setEnabled(false);
        s.setAnimKey("spawn");
    }

    @Override
    public void tick() {
        CastedSpiritBase s = spirit();
        time++;

        switch (phase) {
            case SPAWN -> {
                if (time >= duration) startTravel();
            }

            case TRAVEL -> {
                if (s.collisionController.hasCollision()) {
                    LivingEntity hit = s.collisionController.getCollisionTarget();
                    s.collisionController.resetCollision();
                    if (hit != null && hit.isAlive()) onHit(hit);
                    startOvershoot();
                    return;
                }
                if (time >= duration) startOvershoot();
            }

            case OVERSHOOT -> {
                if (time >= duration) startAscent();
            }

            case ASCENT -> {
                if (time >= duration) startHover();
            }

            case HOVER -> {
                if (!vortexSpawned && time >= 5) {
                    spawnVortex();
                    vortexSpawned = true;
                }
                if (time >= duration) startDespawn();
            }

            case DESPAWN -> {
                if (time >= duration) s.discard();
            }
        }
    }

    @Override
    protected void onTick() {

    }

    private void startTravel() {
        CastedSpiritBase s = spirit();
        phase = Phase.TRAVEL;
        time = 0;
        duration = 8;

        s.flightController.setMovement(s.flightController.dashMovement(duration));
        s.flightController.setEnabled(true);

        s.collisionController.setCollisionCheck(
                s.collisionController.surfaceHitboxCollision(SpiritConstants.COLLISION_RADIUS)
        );
        s.collisionController.setEnabled(true);

        s.orientationController.setEnabled(true);
        s.setAnimKey("travel");
    }

    private void startOvershoot() {
        CastedSpiritBase s = spirit();
        phase = Phase.OVERSHOOT;
        time = 0;
        duration = 2;

        s.flightController.setMovement(
                s.flightController.overshootCharged(1.0, 2)
        );
        s.flightController.setEnabled(true);

        s.collisionController.setEnabled(false);
        s.setAnimKey("travel");
    }

    private void startAscent() {
        CastedSpiritBase s = spirit();
        phase = Phase.ASCENT;
        time = 0;
        duration = 5;

        s.flightController.setMovement(
                s.flightController.ascendCharged(2.0, 5)
        );
        s.flightController.setEnabled(true);

        s.setAnimKey("travel");
    }

    private void startHover() {
        CastedSpiritBase s = spirit();
        LivingEntity t = s.getTarget();
        if (t == null) {
            startDespawn();
            return;
        }

        phase = Phase.HOVER;
        time = 0;
        duration = 10;
        vortexSpawned = false;

        Vec3 current = s.position();
        Vec3 base = new Vec3(t.getX(), t.getY(), t.getZ());
        hoverOffset = current.subtract(base);

        s.flightController.setMovement(
                s.flightController.hoverCharged(hoverOffset)
        );
        s.flightController.setEnabled(true);

        s.collisionController.setEnabled(false);
        s.setAnimKey("travel");
    }

    private void startDespawn() {
        CastedSpiritBase s = spirit();
        phase = Phase.DESPAWN;
        time = 0;
        duration = 7.5;

        s.flightController.setEnabled(false);
        s.collisionController.setEnabled(false);
        s.spiritBounceController.setEnabled(false);
        s.orientationController.setEnabled(false);

        s.setDeltaMovement(Vec3.ZERO);
        s.setAnimKey("despawn");
    }

    private void spawnVortex() {
        CastedSpiritBase s = spirit();
        if (!(s.level() instanceof ServerLevel level)) return;

        LivingEntity t = s.getTarget();
        if (t == null) return;

        Vec3 c = t.getBoundingBox().getCenter();
        double y = t.getBoundingBox().minY;

        WindVortex vortex = new WindVortex(VortexHelperRegistry.WIND_VORTEX.get(), level);
        vortex.setOwner(s.getOwner());
        vortex.setPos(c.x, y, c.z);

        level.addFreshEntity(vortex);
        level.playSound(null, c.x, y, c.z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 0.7F, 1.0F);
    }

    @Override
    public void onHit(LivingEntity target) {
        CastedSpiritBase s = spirit();
        target.hurt(target.damageSources().magic(), InstrumentConstants.CHARGED_DAMAGE);

        if (s.getOwner() instanceof ServerPlayer player) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof InstrumentBase) {
                InstrumentChargeData.reset(stack);
            }
        }
    }
}
