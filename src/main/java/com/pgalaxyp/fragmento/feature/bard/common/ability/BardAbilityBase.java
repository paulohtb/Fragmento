package com.pgalaxyp.fragmento.feature.bard.common.ability;

import com.pgalaxyp.fragmento.core.engine.ability.AbilityRaycastBase;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public abstract class BardAbilityBase extends AbilityRaycastBase {

    @Override
    protected final void onHitEntity(LivingEntity caster, LivingEntity target, Vec3 hitPos) {
        applyToTarget(caster, target, hitPos);
    }

    @Override
    protected final void onHitPosition(LivingEntity caster, Vec3 hitPos) {
        applyToPosition(caster, hitPos);
    }

    protected abstract void applyToTarget(LivingEntity caster, LivingEntity target, Vec3 hitPos);

    protected abstract void applyToPosition(LivingEntity caster, Vec3 hitPos);
}
