package com.pgalaxyp.fragmento.rpg.skill.rule;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg.skill.config.AbilityConfig;
import com.pgalaxyp.fragmento.rpg.state.runtime.AbilityState;
import com.pgalaxyp.fragmento.rpg.state.runtime.EquippedSkillsState;

import java.util.HashMap;
import java.util.Map;

public final class InfusedRule {

    private final CooldownRule cooldownRule;
    private final AbilityConfig config;

    public InfusedRule(CooldownRule cooldownRule, AbilityConfig config) {
        this.cooldownRule = cooldownRule;
        this.config = config;
    }

    public AbilityState toggle(
            AbilityState state,
            EquippedSkillsState equipped,
            AbilityIntent intent,
            Time now
    ) {
        if (state == null || equipped == null || intent == null || now == null) {
            return state;
        }
        if (intent.kind() != AbilityIntentKind.TOGGLE) {
            return state;
        }

        SkillSlotId slot = intent.slot();
        SkillId skillId = equipped.skillInSlot(slot);
        if (skillId == null) {
            return state;
        }

        if (!cooldownRule.ready(state, skillId, now)) {
            return state;
        }

        Map<SkillSlotId, SkillId> infused = new HashMap<>(state.infusedArmed());
        if (infused.containsKey(slot)) {
            infused.remove(slot);
        } else {
            infused.put(slot, skillId);
        }

        return new AbilityState(
                state.cooldownEndsAt(),
                infused,
                state.casting()
        );
    }

    public ConsumeResult consumeFirstArmed(
            AbilityState state,
            Time now
    ) {
        if (state == null || now == null) {
            return new ConsumeResult(state, null);
        }
        if (state.infusedArmed().isEmpty()) {
            return new ConsumeResult(state, null);
        }

        SkillSlotId chosenSlot = null;
        int best = Integer.MAX_VALUE;

        for (SkillSlotId slot : state.infusedArmed().keySet()) {
            if (slot != null && slot.index() < best) {
                best = slot.index();
                chosenSlot = slot;
            }
        }

        if (chosenSlot == null) {
            return new ConsumeResult(state, null);
        }

        SkillId skillId = state.infusedArmed().get(chosenSlot);
        if (skillId == null) {
            return new ConsumeResult(state, null);
        }

        Map<SkillSlotId, SkillId> infused = new HashMap<>(state.infusedArmed());
        infused.remove(chosenSlot);

        AbilityState next = new AbilityState(
                state.cooldownEndsAt(),
                infused,
                state.casting()
        );

        return new ConsumeResult(next, skillId);
    }

    public AbilityState startCooldownOnUse(
            AbilityState state,
            SkillId skillId,
            Time now
    ) {
        if (state == null || skillId == null || now == null) {
            return state;
        }
        return cooldownRule.startCooldown(state, skillId, config.cooldownDuration(skillId), now);
    }

    public record ConsumeResult(
            AbilityState state,
            SkillId consumedSkillId
    ) {}
}