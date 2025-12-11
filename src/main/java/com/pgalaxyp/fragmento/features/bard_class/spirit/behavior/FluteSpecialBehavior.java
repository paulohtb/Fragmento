package com.pgalaxyp.fragmento.features.bard_class.spirit.behavior;

import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentConstants;
import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentCooldownService;
import com.pgalaxyp.fragmento.features.bard_class.registry.entity.VortexHelperRegistry;
import com.pgalaxyp.fragmento.features.bard_class.spirit.base.CastedSpiritBase;
import com.pgalaxyp.fragmento.features.bard_class.spirit.type.WindVortex;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class FluteSpecialBehavior extends SpiritBehavior {

    private enum Phase { SPAWN, ORBIT, ASCENT, HOVER, DESPAWN }

    private int time;
    private Phase phase;
    private Vec3 fixedPos;
    private double duration;

    public FluteSpecialBehavior(CastedSpiritBase spirit) {
        super(spirit);
        startSpawn(Phase.SPAWN, 7.5, spirit, 0);
    }

    @Override
    public void tick() {
        CastedSpiritBase spirit = spirit();
        time++;

        switch (phase) {
            case SPAWN -> {
                if (time >= duration) {
                    startOrbit(Phase.ORBIT, 22.5, spirit, 0);
                }
            }
            case ORBIT -> {
                if (time >= duration) {
                    fixedPos = spirit.position();
                    startAscent(Phase.ASCENT, 10, spirit, 0);
                }
            }
            case ASCENT -> {
                if (time >= duration) {
                    startHover(Phase.HOVER, 100, spirit, 0);
                }
            }
            case HOVER -> {
                if (time >= duration) {
                    startDespawn(Phase.DESPAWN, 7.5, spirit, 0);
                }
            }
            case DESPAWN -> {
                if (time >= duration) {
                    spirit.discard();
                }
            }
        }
    }

    @Override
    protected void onTick() {
    }

    public boolean isInterruptible() {
        return phase == Phase.SPAWN
                || phase == Phase.ORBIT
                || phase == Phase.ASCENT;
    }

    private void startSpawn(Phase phase, double duration, CastedSpiritBase spirit, int time) {
        this.phase = phase;
        this.duration = duration;
        this.time = time;

        spirit.flightController.setEnabled(false);
        spirit.orientationController.setEnabled(false);

        spirit.setAnimKey("spawn");
    }

    private void startOrbit(Phase phase, double duration, CastedSpiritBase spirit, int time) {
        this.phase = phase;
        this.duration = duration;
        this.time = time;

        spirit.flightController.setEnabled(true);
        spirit.orientationController.setEnabled(true);

        spirit.setAnimKey("travel");
        spirit.flightController.setMovement(
                spirit.flightController.orbitMovement(2D, duration)
        );
    }

    private void startAscent(Phase phase, double duration, CastedSpiritBase spirit, int time) {
        this.phase = phase;
        this.duration = duration;
        this.time = time;

        spirit.flightController.setEnabled(true);
        spirit.orientationController.setEnabled(true);

        spirit.setAnimKey("travel");
        spirit.flightController.setMovement(
                spirit.flightController.ascendCharged(2D, duration)
        );
    }

    private void startHover(Phase phase, double duration, CastedSpiritBase spirit, int time) {
        this.phase = phase;
        this.duration = duration;
        this.time = time;

        this.fixedPos = spirit.position();

        spirit.flightController.setEnabled(true);
        spirit.orientationController.setEnabled(true);

        spirit.flightController.setMovement(
                (self, target, age) -> {
                    Vec3 delta = fixedPos.subtract(self.position());
                    self.setDeltaMovement(delta);
                }
        );

        spirit.setAnimKey("travel");

        // Efeito visual e mecânico
        spawnEffect(spirit);

        // Aqui é o ponto certo para aplicar o cooldown
        // O espírito já passou por SPAWN, ORBIT e ASCENT
        // Agora entrou no HOVER, que é a parte ativa da habilidade
        applyCooldownOnOwnerInstrument(spirit);
    }

    private void startDespawn(Phase phase, double duration, CastedSpiritBase spirit, int time) {
        this.phase = phase;
        this.duration = duration;
        this.time = time;

        spirit.flightController.setEnabled(false);
        spirit.orientationController.setEnabled(false);

        spirit.setAnimKey("despawn");
    }

    private void spawnEffect(CastedSpiritBase spirit) {
        if (!(spirit.level() instanceof ServerLevel level)) {
            return;
        }

        LivingEntity owner = spirit.getOwner();
        if (owner == null) {
            return;
        }

        Vec3 c = fixedPos;

        WindVortex vortex = new WindVortex(VortexHelperRegistry.WIND_VORTEX.get(), level);
        vortex.setOwner(owner);
        vortex.setPos(c.x, c.y, c.z);

        level.addFreshEntity(vortex);
        level.playSound(
                null,
                c.x,
                c.y,
                c.z,
                net.minecraft.sounds.SoundEvents.ENCHANTMENT_TABLE_USE,
                net.minecraft.sounds.SoundSource.PLAYERS,
                0.7F,
                1.0F
        );
    }

    private void applyCooldownOnOwnerInstrument(CastedSpiritBase spirit) {
        LivingEntity owner = spirit.getOwner();
        if (!(owner instanceof ServerPlayer player)) {
            return;
        }

        ItemStack main = player.getMainHandItem();
        ItemStack off = player.getOffhandItem();

        if (main.getItem() instanceof InstrumentBase instrument) {
            InstrumentCooldownService.applyCooldown(player, instrument, InstrumentConstants.SPECIAL_COOLDOWN);
            return;
        }

        if (off.getItem() instanceof InstrumentBase instrument) {
            InstrumentCooldownService.applyCooldown(player, instrument, InstrumentConstants.SPECIAL_COOLDOWN);
        }
    }

    @Override
    public void onHit(LivingEntity target) {
    }
}
