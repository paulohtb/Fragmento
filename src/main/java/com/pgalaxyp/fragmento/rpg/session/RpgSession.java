package com.pgalaxyp.fragmento.rpg.session;

import com.pgalaxyp.fragmento.rpg.combat.engine.CombatEngine;
import com.pgalaxyp.fragmento.rpg.combat.rule.ExecutionLifecycleRule;
import com.pgalaxyp.fragmento.rpg.content.profile.CatalystProfile;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.execution.ExecutionFacts;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;
import com.pgalaxyp.fragmento.rpg.engine.catalyst.CatalystEffectAdapter;
import com.pgalaxyp.fragmento.rpg.loadout.LoadoutUpdater;
import com.pgalaxyp.fragmento.rpg.registry.RpgRegistry;
import com.pgalaxyp.fragmento.rpg.skill.runtime.AbilityEngine;
import com.pgalaxyp.fragmento.rpg.state.runtime.ActionLockState;
import com.pgalaxyp.fragmento.rpg.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RpgSession {

    private final ServerPlayer player;
    private final CombatEngine combatEngine;
    private final AbilityEngine abilityEngine;
    private final LoadoutUpdater loadoutUpdater;
    private final ExecutionLifecycleRule executionLifecycle = new ExecutionLifecycleRule();

    private ServerCombatState state;

    public RpgSession(
            ServerPlayer player,
            CombatEngine combatEngine,
            AbilityEngine abilityEngine,
            LoadoutUpdater loadoutUpdater,
            ServerCombatState initial
    ) {
        this.player = player;
        this.combatEngine = combatEngine;
        this.abilityEngine = abilityEngine;
        this.loadoutUpdater = loadoutUpdater;
        this.state = initial;
    }

    public Update onAttackIntent(
            AttackIntent intent,
            ExecutionFacts facts,
            Time now
    ) {
        ServerCombatState before = state;

        state = applyExecutionFacts(state, facts, now);
        state = loadoutUpdater.update(state, player);
        state = combatEngine.applyAttack(state, intent, now);

        return collectTransitions(before, state, now);
    }

    public Update onAbilityIntent(
            AbilityIntent intent,
            ExecutionFacts facts,
            Time now
    ) {
        ServerCombatState before = state;

        state = applyExecutionFacts(state, facts, now);
        state = loadoutUpdater.update(state, player);
        state = abilityEngine.apply(state, intent, now);

        return collectTransitions(before, state, now);
    }

    public Update tick(
            ExecutionFacts facts,
            Time now
    ) {
        ServerCombatState before = state;

        state = applyExecutionFacts(state, facts, now);
        state = loadoutUpdater.update(state, player);
        state = combatEngine.tick(state, now);
        state = abilityEngine.tick(state, now);

        return collectTransitions(before, state, now);
    }

    public ServerCombatState state() {
        return state;
    }

    private ServerCombatState applyExecutionFacts(
            ServerCombatState s,
            ExecutionFacts facts,
            Time now
    ) {
        if (s == null) return s;
        return s.withExecution(
                executionLifecycle.refresh(s.execution(), facts, now)
        );
    }

    private Update collectTransitions(
            ServerCombatState before,
            ServerCombatState after,
            Time now
    ) {
        List<RpgEffect> effects = new ArrayList<>();

        CatalystProfile profile =
                RpgRegistry.profiles().resolve(after.loadout().family());

        CatalystEffectAdapter adapter =
                profile != null ? profile.effects() : null;

        if (adapter == null) {
            return new Update(after, List.of());
        }

        if (after.combo().stepIndex() != before.combo().stepIndex()) {
            effects.addAll(
                    adapter.onAction(
                            player,
                            ActionKind.COMBO_STEP,
                            after.combo().stepIndex(),
                            null,
                            now
                    )
            );
        }

        ActionLockState b = before.lock();
        ActionLockState a = after.lock();

        if (a != null) {
            ActionKind k = a.actionKind();
            boolean trigger =
                    k == ActionKind.INFUSED_EXECUTE
                            || k == ActionKind.CAST_FINISH;

            if (trigger) {
                boolean changed =
                        k != (b != null ? b.actionKind() : null)
                                || !Objects.equals(a.skillId(), b != null ? b.skillId() : null)
                                || !Objects.equals(a.endsAt(), b != null ? b.endsAt() : null);

                if (changed) {
                    effects.addAll(
                            adapter.onAction(
                                    player,
                                    k,
                                    after.combo().stepIndex(),
                                    a.skillId(),
                                    now
                            )
                    );
                }
            }
        }

        return new Update(after, List.copyOf(effects));
    }

    public record Update(
            ServerCombatState state,
            List<RpgEffect> effects
    ) {}
}