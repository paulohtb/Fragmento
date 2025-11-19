package com.pgalaxyp.fragmento.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class PushEffect extends MobEffect {

    public PushEffect() {
        super(MobEffectCategory.HARMFUL, 0x88CCFF); // cor azul vento
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true; // ticka sempre
    }

    @Override
    public boolean applyEffectTick(LivingEntity affected, int amplifier) {

        if (affected.level().isClientSide())
            return true;

        double radius = 4.0D + amplifier; // raio do redemoinho
        double strength = 0.08D + amplifier * 0.03D; // força do vento

        // entidades dentro do vento
        List<LivingEntity> nearby = affected.level().getEntitiesOfClass(
                LivingEntity.class,
                affected.getBoundingBox().inflate(radius),
                (e) -> e != affected
        );

        // vento giratório
        long gameTime = affected.level().getGameTime();
        double windAngle = (gameTime % 360) * 0.12D; // rotação contínua

        for (LivingEntity entity : nearby) {

            // posição relativa ao centro
            Vec3 offset = entity.position().subtract(affected.position());
            double dist = offset.length();

            if (dist < 0.001) continue;

            // coordenadas normalizadas
            double nx = offset.x / dist;
            double nz = offset.z / dist;

            // ângulo giratório aplicando o vento circular
            double cos = Math.cos(windAngle);
            double sin = Math.sin(windAngle);

            // rotação da corrente de vento (Efeito de rio circular)
            double rx = nx * cos - nz * sin;
            double rz = nx * sin + nz * cos;

            // movimento final
            Vec3 push = new Vec3(rx, 0, rz).scale(strength);

            // aplica correnteza
            entity.setDeltaMovement(
                    entity.getDeltaMovement().add(push)
            );

            // mantém as entidades "presas" no fluxo
            if (dist > radius * 0.6) {
                // empurra para perto do eixo
                Vec3 towardCenter = affected.position().subtract(entity.position()).normalize().scale(strength * 0.6);
                entity.setDeltaMovement(entity.getDeltaMovement().add(towardCenter));
            }

            entity.hurtMarked = true;
        }

        return true;
    }
}