package com.pgalaxyp.fragmento.rpg_old.runtime;

import com.pgalaxyp.fragmento.rpg_old.combat.config.ComboConfig;
import com.pgalaxyp.fragmento.rpg_old.combat.engine.CombatEngine;
import com.pgalaxyp.fragmento.rpg_old.combat.rule.ComboResetRule;
import com.pgalaxyp.fragmento.rpg_old.combat.rule.ComboRule;
import com.pgalaxyp.fragmento.rpg_old.combat.rule.ComboTimingRule;
import com.pgalaxyp.fragmento.rpg_old.combat.rule.ExecutionGateRule;
import com.pgalaxyp.fragmento.rpg_old.combat.rule.HoldLatchRule;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Duration;
import com.pgalaxyp.fragmento.rpg_old.lock.rule.ActionLockRule;
import com.pgalaxyp.fragmento.rpg_old.lock.rule.EquipGateRule;
import com.pgalaxyp.fragmento.rpg_old.lock.rule.LockGateRule;
import com.pgalaxyp.fragmento.rpg_old.registry.RpgRegistry;
import com.pgalaxyp.fragmento.rpg_old.session.RpgSessionManager;
import com.pgalaxyp.fragmento.rpg_old.skill.config.AbilityConfig;
import com.pgalaxyp.fragmento.rpg_old.skill.config.SkillTuning;
import com.pgalaxyp.fragmento.rpg_old.skill.rule.CastedRule;
import com.pgalaxyp.fragmento.rpg_old.skill.rule.CooldownRule;
import com.pgalaxyp.fragmento.rpg_old.skill.rule.InfusedRule;
import com.pgalaxyp.fragmento.rpg_old.skill.runtime.AbilityEngine;

public final class RpgWiring {

    private static final ComboConfig COMBO_CONFIG = new DefaultComboConfig();
    private static final AbilityConfig ABILITY_CONFIG = new RegistryAbilityConfig();

    public static CombatEngine createCombatEngine() {
        EquipGateRule equipGate = new EquipGateRule();
        LockGateRule lockGate = new LockGateRule();
        ExecutionGateRule executionGate = new ExecutionGateRule();
        ActionLockRule actionLockRule = new ActionLockRule();

        CooldownRule cooldownRule = new CooldownRule();
        InfusedRule infusedRule = new InfusedRule(cooldownRule, ABILITY_CONFIG);

        return new CombatEngine(
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
    }

    public static AbilityEngine createAbilityEngine() {
        EquipGateRule equipGate = new EquipGateRule();
        LockGateRule lockGate = new LockGateRule();
        ExecutionGateRule executionGate = new ExecutionGateRule();
        ActionLockRule actionLockRule = new ActionLockRule();

        CooldownRule cooldownRule = new CooldownRule();
        InfusedRule infusedRule = new InfusedRule(cooldownRule, ABILITY_CONFIG);
        CastedRule castedRule = new CastedRule(cooldownRule, ABILITY_CONFIG);

        return new AbilityEngine(
                equipGate,
                lockGate,
                executionGate,
                ABILITY_CONFIG,
                actionLockRule,
                infusedRule,
                castedRule
        );
    }

    public static RpgSessionManager createSessionManager() {
        return new RpgSessionManager(
                createCombatEngine(),
                createAbilityEngine()
        );
    }

    private RpgWiring() {}

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
            return Duration.ofTicks(20);
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
            return t != null && t.castDuration() != null
                    ? t.castDuration()
                    : Duration.ofTicks(0);
        }

        @Override
        public Duration cooldownDuration(SkillId skillId) {
            SkillTuning t = tuning(skillId);
            return t != null && t.cooldownDuration() != null
                    ? t.cooldownDuration()
                    : Duration.ofTicks(0);
        }

        @Override
        public Duration cancelCooldownDuration(SkillId skillId) {
            SkillTuning t = tuning(skillId);
            return t != null && t.cancelCooldownDuration() != null
                    ? t.cancelCooldownDuration()
                    : Duration.ofTicks(0);
        }

        @Override
        public Duration actionLockDuration(SkillId skillId) {
            SkillTuning t = tuning(skillId);
            return t != null && t.actionLockDuration() != null
                    ? t.actionLockDuration()
                    : Duration.ofTicks(0);
        }

        @Override
        public Duration executionEntityLife(SkillId skillId) {
            SkillTuning t = tuning(skillId);
            return t != null && t.executionEntityLife() != null
                    ? t.executionEntityLife()
                    : Duration.ofTicks(0);
        }
    }
}