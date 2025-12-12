package com.pgalaxyp.fragmento.features.bard_class.spirit.behavior;

import com.pgalaxyp.fragmento.features.bard_class.instrument.*;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
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

        spirit.setAnimKey("spawn");
        spirit.flightController.setEnabled(false);
        spirit.collisionController.setEnabled(false);
        spirit.orientationController.setEnabled(false);
        spirit.spiritBounceController.setEnabled(false);
    }

    @Override
    public void tick() {
        CastedSpiritBase s = spirit();
        time++;

        try {
            if (phase == Phase.TRAVEL && s.getTarget() == null) {
                startDespawn();
                return;
            }

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
                                s.collisionController.surfaceHitboxCollision(0.2)
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
        } catch (Exception e) {
            startDespawn();
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
            ItemStack snap = s.getInstrumentSnapshot();
            for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
                ItemStack inv = p.getInventory().getItem(i);
                if (ItemStack.isSameItemSameComponents(inv, snap)) {
                    InstrumentChargeData.increment(inv);
                    break;
                }
            }
        }

        target.hurt(target.damageSources().magic(), InstrumentConstants.BASIC_DAMAGE);
    }
}
