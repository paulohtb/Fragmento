package com.pgalaxyp.fragmento.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class UpstreamEffect extends MobEffect {

    public UpstreamEffect() { super(MobEffectCategory.BENEFICIAL, 0x80E0FF); }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!(entity instanceof Mob)) return false;

        var tag = entity.getPersistentData();
        if (!tag.contains("fragmento.upstream_start_y")) {
            tag.putDouble("fragmento.upstream_start_y", entity.getY());
        }

        double startY = tag.getDouble("fragmento.upstream_start_y");
        double currentY = entity.getY();
        double deltaY = currentY - startY;

        if (deltaY < 5.0D) {
            double maxHeight = 5.0D;
            double totalTicks = 100.0D;
            double upwardSpeed = maxHeight / totalTicks;
            entity.setDeltaMovement(new Vec3(
                    entity.getDeltaMovement().x,
                    upwardSpeed,
                    entity.getDeltaMovement().z
            ));
            entity.hasImpulse = true;
        }
        else {
            entity.setDeltaMovement(new Vec3(
                    entity.getDeltaMovement().x,
                    0.0D,
                    entity.getDeltaMovement().z
            ));
            entity.hasImpulse = false;
        }

        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) { return true; }
}