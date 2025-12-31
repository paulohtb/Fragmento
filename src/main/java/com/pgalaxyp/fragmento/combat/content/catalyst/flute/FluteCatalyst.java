package com.pgalaxyp.fragmento.combat.content.catalyst.flute;

import com.pgalaxyp.fragmento.combat.content.catalyst.Catalyst;
import com.pgalaxyp.fragmento.combat.content.entity.flute.FluteBasicHitEntity;
import com.pgalaxyp.fragmento.combat.content.entity.flute.FluteCastingEntity;
import com.pgalaxyp.fragmento.combat.content.entity.flute.FluteVortexEntity;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.engine.entity.FragmentoEntities;
import com.pgalaxyp.fragmento.combat.rule.skill.CastingSkillRule;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class FluteCatalyst implements Catalyst {

    @Override
    public void onAttack(
            ServerPlayer player,
            ServerLevel level,
            ServerCombatState state,
            CombatTime now
    ) {
        if (state.infusion().consumeIfArmed()) {
            spawnInfusedAttack(player, level, state);
            return;
        }

        spawnBasicAttack(player, level, state);
    }

    @Override
    public void onCastingTick(
            ServerPlayer player,
            ServerLevel level,
            ServerCombatState state,
            CastingSkillRule castingRule,
            CombatTime now
    ) {
        if (!castingRule.isCastComplete(state, now)) {
            return;
        }

        long targetId = castingRule.finishCasting(state);
        spawnCastingAttack(player, level, targetId);
    }

    private void spawnBasicAttack(
            ServerPlayer player,
            ServerLevel level,
            ServerCombatState state
    ) {
        FluteBasicHitEntity entity =
                new FluteBasicHitEntity(
                        FragmentoEntities.FLUTE_BASIC_HIT.get(),
                        level
                );

        entity.configure(player, state);
        level.addFreshEntity(entity);
    }

    private void spawnInfusedAttack(
            ServerPlayer player,
            ServerLevel level,
            ServerCombatState state
    ) {
        FluteVortexEntity entity =
                new FluteVortexEntity(
                        FragmentoEntities.FLUTE_VORTEX.get(),
                        level
                );

        entity.configure(player, state);
        level.addFreshEntity(entity);
    }

    private void spawnCastingAttack(
            ServerPlayer player,
            ServerLevel level,
            long targetId
    ) {
        FluteCastingEntity entity =
                new FluteCastingEntity(
                        FragmentoEntities.FLUTE_CASTING.get(),
                        level
                );

        entity.configure(player, targetId);
        level.addFreshEntity(entity);
    }
}