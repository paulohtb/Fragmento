package com.pgalaxyp.fragmento.combat.rule.ability;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.rule.cooldown.CooldownRule;
import com.pgalaxyp.fragmento.combat.rule.port.AbilityConfig;
import com.pgalaxyp.fragmento.combat.state.runtime.AbilityRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.EquippedSkillsRuntimeState;

import java.util.HashMap;
import java.util.Map;

public final class InfusedRule {

    private final CooldownRule cooldownRule;
    private final AbilityConfig config;

    public InfusedRule(CooldownRule cooldownRule, AbilityConfig config) {
        this.cooldownRule = cooldownRule;
        this.config = config;
    }

    public AbilityRuntimeState toggle(
            AbilityRuntimeState state,
            EquippedSkillsRuntimeState equipped,
            AbilityIntent intent,
            CombatTime now
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

        return new AbilityRuntimeState(
                state.cooldownEndsAt(),
                infused,
                state.casting()
        );
    }

    public ConsumeResult consumeFirstArmed(
            AbilityRuntimeState state,
            CombatTime now
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

        AbilityRuntimeState next = new AbilityRuntimeState(
                state.cooldownEndsAt(),
                infused,
                state.casting()
        );

        return new ConsumeResult(next, skillId);
    }

    public AbilityRuntimeState startCooldownOnUse(
            AbilityRuntimeState state,
            SkillId skillId,
            CombatTime now
    ) {
        if (state == null || skillId == null || now == null) {
            return state;
        }
        return cooldownRule.startCooldown(state, skillId, config.cooldownDuration(skillId), now);
    }

    public record ConsumeResult(
            AbilityRuntimeState state,
            SkillId consumedSkillId
    ) {}
}