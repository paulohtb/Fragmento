package com.pgalaxyp.fragmento.features.bard_class.spirit.behavior;

import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentChargeData;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentConstants;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.controller.SpiritConstants;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class FluteBasicBehavior implements CastedSpiritBase.SpiritBehavior {

    private enum Phase { SPAWN, ACTIVE, DESPAWN }

    private Phase phase;
    private int time;
    private int duration;

    @Override
    public void init(CastedSpiritBase spirit) {
        phase = Phase.SPAWN;
        time = 0;
        duration = 10;

        spirit.setAnimKey("spawn");

        spirit.flightController.setEnabled(false);
        spirit.collisionController.setEnabled(false);
        spirit.spiritBounceController.setEnabled(false);
    }

    @Override
    public void tick(CastedSpiritBase spirit) {
        time++;

        switch (phase) {

            case SPAWN -> {
                if (time >= duration) {
                    phase = Phase.ACTIVE;
                    time = 0;
                    duration = 20;

                    spirit.flightController.setMovement(
                            spirit.flightController.dashMovement(duration)
                    );

                    spirit.flightController.setEnabled(true);
                    spirit.collisionController.setEnabled(true);
                    spirit.spiritBounceController.setEnabled(true);

                    spirit.collisionController.setCollisionCheck(
                            spirit.collisionController.surfaceHitboxCollision(SpiritConstants.COLLISION_RADIUS)
                    );

                    spirit.setAnimKey("travel");
                }
            }

            case ACTIVE -> {
                if (spirit.collisionController.hasCollision()) {
                    LivingEntity target = spirit.collisionController.getCollisionTarget();
                    spirit.collisionController.resetCollision();

                    if (target != null && target.isAlive()) {
                        onHit(spirit, target);
                        spirit.spiritBounceController.bounce();
                    }

                    despawnInit(spirit);
                }

                if (time >= duration) {
                    despawnInit(spirit);
                }
            }

            case DESPAWN -> {
                if (time >= duration) {
                    spirit.discard();
                }
            }
        }
    }

    private void despawnInit(CastedSpiritBase spirit) {
        phase = Phase.DESPAWN;
        time = 0;
        duration = 10;

        spirit.flightController.setEnabled(false);
        spirit.collisionController.setEnabled(false);
        spirit.spiritBounceController.setEnabled(false);

        spirit.setAnimKey("despawn");
    }

    @Override
    public void onHit(CastedSpiritBase spirit, LivingEntity target) {
        int currentCharge = 0;
        if (spirit.getOwner() instanceof ServerPlayer p) {
            ItemStack s = p.getMainHandItem();
            if (s.getItem() instanceof InstrumentBase) {
                currentCharge = InstrumentChargeData.getCharge(s);
            }
        }

        target.hurt(target.damageSources().magic(), InstrumentConstants.BASIC_DAMAGE);

        if (spirit.getOwner() instanceof ServerPlayer player) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof InstrumentBase) {
                InstrumentChargeData.increment(stack);
            }
        }
    }
}