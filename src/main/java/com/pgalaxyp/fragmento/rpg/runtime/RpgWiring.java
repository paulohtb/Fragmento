package com.pgalaxyp.fragmento.rpg.runtime;

import com.pgalaxyp.fragmento.rpg.content.skill.BardSkills;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg.session.RpgSessionManager;
import com.pgalaxyp.fragmento.rpg.skill.rule.CastedRule;
import com.pgalaxyp.fragmento.rpg.skill.rule.InfusedRule;
import com.pgalaxyp.fragmento.rpg.skill.runtime.AbilityEngine;
import com.pgalaxyp.fragmento.rpg.lock.rule.ActionLockRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboResetRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboTimingRule;
import com.pgalaxyp.fragmento.rpg.combat.engine.CombatEngine;
import com.pgalaxyp.fragmento.rpg.combat.rule.HoldLatchRule;
import com.pgalaxyp.fragmento.rpg.skill.rule.CooldownRule;
import com.pgalaxyp.fragmento.rpg.lock.rule.EquipGateRule;
import com.pgalaxyp.fragmento.rpg.lock.rule.LockGateRule;
import com.pgalaxyp.fragmento.rpg.skill.config.AbilityConfig;
import com.pgalaxyp.fragmento.rpg.combat.config.ComboConfig;

public final class RpgWiring {

    private static final ComboConfig COMBO_CONFIG = new DefaultComboConfig();
    private static final AbilityConfig ABILITY_CONFIG = new DefaultAbilityConfig();

    public static RpgSessionManager createSessionManager() {
        EquipGateRule equipGate = new EquipGateRule();
        LockGateRule lockGate = new LockGateRule();
        CooldownRule cooldownRule = new CooldownRule();
        ActionLockRule actionLockRule = new ActionLockRule();

        InfusedRule infusedRule = new InfusedRule(cooldownRule, ABILITY_CONFIG);
        CastedRule castedRule = new CastedRule(cooldownRule, ABILITY_CONFIG);

        CombatEngine combatEngine = new CombatEngine(
                equipGate,
                lockGate,
                COMBO_CONFIG,
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
                ABILITY_CONFIG,
                actionLockRule,
                infusedRule,
                castedRule
        );

        return new RpgSessionManager(combatEngine, abilityEngine);
    }

    private static final class DefaultComboConfig implements ComboConfig {
        @Override
        public int maxSteps() {
            return 3;
        }

        @Override
        public Duration stepDuration() {
            return Duration.ofTicks(10);
        }

        @Override
        public Duration actionLockDuration() {
            return Duration.ofTicks(10);
        }
    }

    private static final class DefaultAbilityConfig implements AbilityConfig {

        @Override
        public Duration castDuration(SkillId skillId) {
            if (skillId != null && skillId.value() == BardSkills.BARDO_SPECIAL_CASTED.value()) {
                return Duration.ofTicks(40);
            }
            return Duration.ofTicks(0);
        }

        @Override
        public Duration cooldownDuration(SkillId skillId) {
            if (skillId != null && skillId.value() == BardSkills.BARDO_NORMAL_INFUSED.value()) {
                return Duration.ofTicks(200);
            }
            if (skillId != null && skillId.value() == BardSkills.BARDO_SPECIAL_CASTED.value()) {
                return Duration.ofTicks(400);
            }
            return Duration.ofTicks(0);
        }

        @Override
        public Duration actionLockDuration(SkillId skillId) {
            return Duration.ofTicks(10);
        }
    }

    private RpgWiring() {}
}