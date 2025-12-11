package com.pgalaxyp.fragmento.features.bard_class.spirit.behavior;

import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentChargeData;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentConstants;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.controller.SpiritConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FluteBasicBehavior extends SpiritBehavior {

    private enum Phase { SPAWN, TRAVEL, DESPAWN }

    private Phase phase;
    private int time;
    private double duration;

    public FluteBasicBehavior(CastedSpiritBase spirit) {
        super(spirit);
        phase = Phase.SPAWN;
        time = 0;
        duration = 7.5;

        CastedSpiritBase s = spirit();
        s.setAnimKey("spawn");
        s.flightController.setEnabled(false);
        s.collisionController.setEnabled(false);
        s.orientationController.setEnabled(false);
        s.spiritBounceController.setEnabled(false);
    }

    @Override
    public void tick() {
        CastedSpiritBase s = spirit();
        time++;

        switch (phase) {
            case SPAWN -> {
                if (time >= duration) {
                    phase = Phase.TRAVEL;
                    time = 0;
                    duration = 10;

                    s.flightController.setMovement(s.flightController.dashMovement(duration));
                    s.flightController.setEnabled(true);
                    s.collisionController.setEnabled(true);
                    s.spiritBounceController.setEnabled(true);

                    s.collisionController.setCollisionCheck(
                            s.collisionController.surfaceHitboxCollision(SpiritConstants.COLLISION_RADIUS)
                    );

                    s.setAnimKey("travel");
                }
            }

            case TRAVEL -> {
                if (s.collisionController.hasCollision()) {
                    LivingEntity target = s.collisionController.getCollisionTarget();
                    s.collisionController.resetCollision();

                    if (target != null && target.isAlive()) {
                        onHit(target);
                        s.flightController.setEnabled(false);
                        s.collisionController.setEnabled(false);
                        s.setDeltaMovement(Vec3.ZERO);
                        s.spiritBounceController.bounce();
                    }

                    startDespawn();
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

    private void startDespawn() {
        CastedSpiritBase s = spirit();
        phase = Phase.DESPAWN;
        time = 0;
        duration = 7.5;

        s.flightController.setEnabled(false);
        s.collisionController.setEnabled(false);
        s.setAnimKey("despawn");
    }

    @Override
    public void onHit(LivingEntity target) {
        CastedSpiritBase s = spirit();

        if (s.getOwner() instanceof ServerPlayer p) {
            ItemStack stack = p.getMainHandItem();
            if (stack.getItem() instanceof InstrumentBase) {
                InstrumentChargeData.increment(stack);
            }
        }

        target.hurt(target.damageSources().magic(), InstrumentConstants.BASIC_DAMAGE);
    }
}
