package com.pgalaxyp.fragmento.combat.content.catalyst;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.skill.CastingSkillRule;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public interface Catalyst {

    void onAttack(
            ServerPlayer player,
            ServerLevel level,
            ServerCombatState state,
            CombatTime now
    );

    void onCastingTick(
            ServerPlayer player,
            ServerLevel level,
            ServerCombatState state,
            CastingSkillRule castingRule,
            CombatTime now
    );
}