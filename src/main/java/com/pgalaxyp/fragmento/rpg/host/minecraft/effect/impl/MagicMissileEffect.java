package com.pgalaxyp.fragmento.rpg.host.minecraft.effect.impl;

import com.pgalaxyp.fragmento.rpg.core.state.effect.EffectState;
import com.pgalaxyp.fragmento.rpg.host.minecraft.effect.EffectExecutor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public final class MagicMissileEffect implements EffectExecutor {

    @Override
    public void execute(EffectState effect, ServerLevel level) {
        if (!effect.target().isReal()) return;

        var entity = level.getEntity((int) effect.target().actorIdOrZero());
        if (!(entity instanceof LivingEntity living)) return;

        living.hurt(
                level.damageSources().magic(),
                4.0f
        );
    }
}