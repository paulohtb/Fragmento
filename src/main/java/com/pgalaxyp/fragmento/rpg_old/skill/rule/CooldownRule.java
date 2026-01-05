package com.pgalaxyp.fragmento.rpg_old.skill.rule;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.AbilityState;

import java.util.HashMap;
import java.util.Map;

public final class CooldownRule {

    public AbilityState startCooldown(
            AbilityState state,
            SkillId skillId,
            Duration cooldown,
            Time now
    ) {
        if (state == null || skillId == null || cooldown == null || now == null) {
            return state;
        }

        Map<SkillId, Time> cds = new HashMap<>(state.cooldownEndsAt());
        cds.put(skillId, now.plus(cooldown));

        return new AbilityState(
                cds,
                state.infusedArmed(),
                state.casting()
        );
    }

    public boolean ready(
            AbilityState state,
            SkillId skillId,
            Time now
    ) {
        if (state == null || skillId == null || now == null) {
            return false;
        }
        Time ends = state.cooldownEndsAt().get(skillId);
        if (ends == null) {
            return true;
        }
        return now.isAfterOrEqual(ends);
    }
}