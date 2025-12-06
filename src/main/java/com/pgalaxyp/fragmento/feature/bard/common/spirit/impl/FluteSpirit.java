package com.pgalaxyp.fragmento.feature.bard.common.spirit.impl;

import com.pgalaxyp.fragmento.feature.bard.common.combat.BardWeaponProfile;
import com.pgalaxyp.fragmento.feature.bard.common.combat.SpiritCombatProfile;
import com.pgalaxyp.fragmento.feature.bard.common.spirit.BardSpirit;
import com.pgalaxyp.fragmento.feature.bard.common.spirit.SpiritBase;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FluteSpirit extends BardSpirit {

    public FluteSpirit(EntityType<? extends SpiritBase> type, Level level) {
        super(type, level);
    }

    @Override
    protected BardWeaponProfile getProfile() {
        return SpiritCombatProfile.INSTANCE;
    }

    @Override
    protected void applyChargedHitEffects(LivingEntity target) {
        int durationTicks = 3 * 20;
        target.addEffect(new MobEffectInstance(
                MobEffects.GLOWING,
                durationTicks,
                0,
                false,
                true,
                true
        ));
    }

    @Override
    protected void playHitSound(LivingEntity target) {
        target.level().playSound(
                null,
                target.getX(),
                target.getY(),
                target.getZ(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS,
                0.8F,
                1.2F
        );
    }
}
