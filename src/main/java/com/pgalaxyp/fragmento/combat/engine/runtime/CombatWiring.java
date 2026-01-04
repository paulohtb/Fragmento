package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.content.skill.FragmentoSkills;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;
import com.pgalaxyp.fragmento.combat.rule.ability.CastedRule;
import com.pgalaxyp.fragmento.combat.rule.ability.InfusedRule;
import com.pgalaxyp.fragmento.combat.rule.combat.AbilityEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.ActionLockRule;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboResetRule;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboRule;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboTimingRule;
import com.pgalaxyp.fragmento.combat.rule.combat.CombatEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.HoldLatchRule;
import com.pgalaxyp.fragmento.combat.rule.cooldown.CooldownRule;
import com.pgalaxyp.fragmento.combat.rule.gate.EquipGateRule;
import com.pgalaxyp.fragmento.combat.rule.gate.LockGateRule;
import com.pgalaxyp.fragmento.combat.rule.port.AbilityConfig;
import com.pgalaxyp.fragmento.combat.rule.port.ComboConfig;

public final class CombatWiring {

    public static CombatSessionManager createSessionManager() {
        EquipGateRule equipGate = new EquipGateRule();
        LockGateRule lockGate = new LockGateRule();
        CooldownRule cooldownRule = new CooldownRule();
        ActionLockRule actionLockRule = new ActionLockRule();

        ComboConfig comboConfig = new ComboConfig() {
            public int maxSteps() {
                return 3;
            }

            public Duration stepDuration() {
                return Duration.ofTicks(10);
            }

            public Duration actionLockDuration() {
                return Duration.ofTicks(10);
            }
        };

        AbilityConfig abilityConfig = new AbilityConfig() {
            public Duration castDuration(SkillId skillId) {
                if (skillId != null && skillId.value() == FragmentoSkills.BARDO_SPECIAL_CASTED.value()) {
                    return Duration.ofTicks(40);
                }
                return Duration.ofTicks(0);
            }

            public Duration cooldownDuration(SkillId skillId) {
                if (skillId != null && skillId.value() == FragmentoSkills.BARDO_NORMAL_INFUSED.value()) {
                    return Duration.ofTicks(200);
                }
                if (skillId != null && skillId.value() == FragmentoSkills.BARDO_SPECIAL_CASTED.value()) {
                    return Duration.ofTicks(400);
                }
                return Duration.ofTicks(0);
            }

            public Duration actionLockDuration(SkillId skillId) {
                return Duration.ofTicks(10);
            }
        };

        InfusedRule infusedRule = new InfusedRule(cooldownRule, abilityConfig);
        CastedRule castedRule = new CastedRule(cooldownRule, abilityConfig);

        CombatEngine combatEngine = new CombatEngine(
                equipGate,
                lockGate,
                comboConfig,
                actionLockRule,
                new ComboRule(),
                new ComboTimingRule(),
                new ComboResetRule(),
                new HoldLatchRule(),
                infusedRule
        );

        AbilityEngine abilityEngine = new AbilityEngine(
                equipGate,
                lockGate,
                abilityConfig,
                actionLockRule,
                infusedRule,
                castedRule
        );

        return new CombatSessionManager(combatEngine, abilityEngine);
    }

    private CombatWiring() {}
}