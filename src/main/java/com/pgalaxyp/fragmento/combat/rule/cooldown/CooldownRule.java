package com.pgalaxyp.fragmento.combat.rule.cooldown;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;
import com.pgalaxyp.fragmento.combat.state.runtime.AbilityRuntimeState;

import java.util.HashMap;
import java.util.Map;

public final class CooldownRule {

    public AbilityRuntimeState startCooldown(
            AbilityRuntimeState state,
            SkillId skillId,
            Duration cooldown,
            CombatTime now
    ) {
        if (state == null || skillId == null || cooldown == null || now == null) {
            return state;
        }

        Map<SkillId, CombatTime> cds = new HashMap<>(state.cooldownEndsAt());
        cds.put(skillId, now.plus(cooldown));

        return new AbilityRuntimeState(
                cds,
                state.infusedArmed(),
                state.casting()
        );
    }

    public boolean ready(
            AbilityRuntimeState state,
            SkillId skillId,
            CombatTime now
    ) {
        if (state == null || skillId == null || now == null) {
            return false;
        }
        CombatTime ends = state.cooldownEndsAt().get(skillId);
        if (ends == null) {
            return true;
        }
        return now.isAfterOrEqual(ends);
    }
}