package com.pgalaxyp.fragmento.combat.simulation;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.domain.timing.Duration;
import com.pgalaxyp.fragmento.combat.engine.runtime.CombatRuntime;
import com.pgalaxyp.fragmento.combat.rule.ability.CastedRule;
import com.pgalaxyp.fragmento.combat.rule.ability.InfusedRule;
import com.pgalaxyp.fragmento.combat.rule.combat.AbilityEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.ActionLockRule;
import com.pgalaxyp.fragmento.combat.rule.combat.CombatEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboResetRule;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboRule;
import com.pgalaxyp.fragmento.combat.rule.combat.ComboTimingRule;
import com.pgalaxyp.fragmento.combat.rule.combat.HoldLatchRule;
import com.pgalaxyp.fragmento.combat.rule.cooldown.CooldownRule;
import com.pgalaxyp.fragmento.combat.rule.gate.EquipGateRule;
import com.pgalaxyp.fragmento.combat.rule.gate.LockGateRule;
import com.pgalaxyp.fragmento.combat.rule.port.AbilityConfig;
import com.pgalaxyp.fragmento.combat.rule.port.ComboConfig;
import com.pgalaxyp.fragmento.combat.rule.port.CombatClock;
import com.pgalaxyp.fragmento.combat.state.runtime.EquippedSkillsRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import java.util.Map;

public final class CombatSimulation {

    public static CombatRuntime simpleSimulation() {
        CombatClock clock = () -> CombatTime.ofTicks(0L);

        ComboConfig comboConfig = new ComboConfig() {
            @Override
            public int maxSteps() {
                return 3;
            }

            @Override
            public Duration stepDuration() {
                return Duration.ofTicks(10L);
            }

            @Override
            public Duration actionLockDuration() {
                return Duration.ofTicks(10L);
            }
        };

        AbilityConfig abilityConfig = new AbilityConfig() {
            @Override
            public Duration castDuration(SkillId skillId) {
                return Duration.ofTicks(20L);
            }

            @Override
            public Duration cooldownDuration(SkillId skillId) {
                return Duration.ofTicks(40L);
            }

            @Override
            public Duration actionLockDuration(SkillId skillId) {
                return Duration.ofTicks(10L);
            }
        };

        CooldownRule cooldownRule = new CooldownRule();
        ActionLockRule actionLockRule = new ActionLockRule();

        InfusedRule infusedRule = new InfusedRule(cooldownRule, abilityConfig);
        CastedRule castedRule = new CastedRule(cooldownRule, abilityConfig);

        CombatEngine combatEngine =
                new CombatEngine(
                        new EquipGateRule(),
                        new LockGateRule(),
                        comboConfig,
                        actionLockRule,
                        new ComboRule(),
                        new ComboTimingRule(),
                        new ComboResetRule(),
                        new HoldLatchRule(),
                        infusedRule
                );

        AbilityEngine abilityEngine =
                new AbilityEngine(
                        new EquipGateRule(),
                        new LockGateRule(),
                        abilityConfig,
                        actionLockRule,
                        infusedRule,
                        castedRule
                );

        ServerCombatState initial = ServerCombatState.initial()
                .withEquippedSkills(new EquippedSkillsRuntimeState(
                        Map.of(new SkillSlotId(1), new SkillId(1))
                ));

        return new CombatRuntime(
                clock,
                combatEngine,
                abilityEngine,
                initial
        );
    }

    public static void main(String[] args) {
        CombatRuntime runtime = simpleSimulation();
        runtime.onAttackIntent(AttackIntent.CLICK);
        runtime.onAttackIntent(AttackIntent.HOLD_START);
        runtime.tick();
        runtime.onAbilityIntent(new AbilityIntent(new SkillSlotId(1), AbilityIntentKind.TOGGLE));
        runtime.onAttackIntent(AttackIntent.CLICK);
        runtime.tick();
    }
}