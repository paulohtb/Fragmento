package com.pgalaxyp.fragmento.rpg.runtime;

import com.pgalaxyp.fragmento.rpg.combat.config.ComboConfig;
import com.pgalaxyp.fragmento.rpg.combat.engine.CombatEngine;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboResetRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ComboTimingRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.ExecutionGateRule;
import com.pgalaxyp.fragmento.rpg.combat.rule.HoldLatchRule;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg.lock.rule.ActionLockRule;
import com.pgalaxyp.fragmento.rpg.lock.rule.EquipGateRule;
import com.pgalaxyp.fragmento.rpg.lock.rule.LockGateRule;
import com.pgalaxyp.fragmento.rpg.registry.RpgRegistry;
import com.pgalaxyp.fragmento.rpg.session.RpgSessionManager;
import com.pgalaxyp.fragmento.rpg.skill.config.AbilityConfig;
import com.pgalaxyp.fragmento.rpg.skill.config.SkillTuning;
import com.pgalaxyp.fragmento.rpg.skill.rule.CastedRule;
import com.pgalaxyp.fragmento.rpg.skill.rule.CooldownRule;
import com.pgalaxyp.fragmento.rpg.skill.rule.InfusedRule;
import com.pgalaxyp.fragmento.rpg.skill.runtime.AbilityEngine;

public final class RpgWiring {

    private static final ComboConfig COMBO_CONFIG = new DefaultComboConfig();
    private static final AbilityConfig ABILITY_CONFIG = new RegistryAbilityConfig();

    public static RpgSessionManager createSessionManager() {
        EquipGateRule equipGate = new EquipGateRule();
        LockGateRule lockGate = new LockGateRule();
        CooldownRule cooldownRule = new CooldownRule();
        ActionLockRule actionLockRule = new ActionLockRule();
        ExecutionGateRule executionGate = new ExecutionGateRule();

        InfusedRule infusedRule = new InfusedRule(cooldownRule, ABILITY_CONFIG);
        CastedRule castedRule = new CastedRule(cooldownRule, ABILITY_CONFIG);

        CombatEngine combatEngine = new CombatEngine(
                equipGate,
                lockGate,
                executionGate,
                COMBO_CONFIG,
                actionLockRule,
                new ComboRule(),
                new ComboTimingRule(),
                new ComboResetRule(),
                new HoldLatchRule(),
                infusedRule,
                ABILITY_CONFIG
        );

        AbilityEngine abilityEngine = new AbilityEngine(
                equipGate,
                lockGate,
                executionGate,
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

        @Override
        public Duration executionEntityLife() {
            return Duration.ofTicks(15);
        }
    }

    private static final class RegistryAbilityConfig implements AbilityConfig {

        private SkillTuning tuning(SkillId skillId) {
            if (skillId == null) return null;
            return RpgRegistry.skillTunings().resolve(skillId);
        }

        @Override
        public Duration castDuration(SkillId skillId) {
            SkillTuning t = tuning(skillId);
            return t != null && t.castDuration() != null ? t.castDuration() : Duration.ofTicks(0);
        }

        @Override
        public Duration cooldownDuration(SkillId skillId) {
            SkillTuning t = tuning(skillId);
            return t != null && t.cooldownDuration() != null ? t.cooldownDuration() : Duration.ofTicks(0);
        }

        @Override
        public Duration actionLockDuration(SkillId skillId) {
            SkillTuning t = tuning(skillId);
            return t != null && t.actionLockDuration() != null ? t.actionLockDuration() : Duration.ofTicks(0);
        }

        @Override
        public Duration executionEntityLife(SkillId skillId) {
            SkillTuning t = tuning(skillId);
            return t != null && t.executionEntityLife() != null ? t.executionEntityLife() : Duration.ofTicks(0);
        }
    }

    private RpgWiring() {}
}