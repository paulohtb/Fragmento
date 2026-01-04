package com.pgalaxyp.fragmento.rpg.session;

import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.domain.action.ActionKind;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;
import com.pgalaxyp.fragmento.rpg.engine.catalyst.CatalystEffectAdapter;
import com.pgalaxyp.fragmento.rpg.engine.catalyst.CatalystEffectRegistry;
import com.pgalaxyp.fragmento.rpg.loadout.LoadoutUpdater;
import com.pgalaxyp.fragmento.rpg.skill.runtime.AbilityEngine;
import com.pgalaxyp.fragmento.rpg.combat.engine.CombatEngine;
import com.pgalaxyp.fragmento.rpg.time.RpgClock;
import com.pgalaxyp.fragmento.rpg.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class RpgSession {

    private final RpgClock clock;
    private final CombatEngine combatEngine;
    private final AbilityEngine abilityEngine;
    private final LoadoutUpdater loadoutUpdater;

    private ServerCombatState state;

    public RpgSession(
            RpgClock clock,
            CombatEngine combatEngine,
            AbilityEngine abilityEngine,
            LoadoutUpdater loadoutUpdater,
            ServerCombatState initial
    ) {
        this.clock = clock;
        this.combatEngine = combatEngine;
        this.abilityEngine = abilityEngine;
        this.loadoutUpdater = loadoutUpdater;
        this.state = initial;
    }

    public Update onAttackIntent(ServerPlayer player, AttackIntent intent) {
        Time now = clock.now();

        ServerCombatState before = state;
        state = loadoutUpdater.update(state, player);
        state = combatEngine.applyAttack(state, intent, now);

        return collectTransitions(player, before, state, now);
    }

    public Update onAbilityIntent(ServerPlayer player, AbilityIntent intent) {
        Time now = clock.now();

        ServerCombatState before = state;
        state = loadoutUpdater.update(state, player);
        state = abilityEngine.apply(state, intent, now);

        return collectTransitions(player, before, state, now);
    }

    public Update tick(ServerPlayer player) {
        Time now = clock.now();

        ServerCombatState before = state;
        state = loadoutUpdater.update(state, player);
        state = combatEngine.tick(state, now);
        state = abilityEngine.tick(state, now);

        return collectTransitions(player, before, state, now);
    }

    public ServerCombatState state() {
        return state;
    }

    private Update collectTransitions(
            ServerPlayer player,
            ServerCombatState before,
            ServerCombatState after,
            Time now
    ) {
        List<RpgEffect> effects = new ArrayList<>();

        CatalystEffectAdapter adapter =
                CatalystEffectRegistry.resolve(after.loadout().family());

        if (adapter == null) {
            return new Update(after, List.of());
        }

        int beforeStep = before.combo().stepIndex();
        int afterStep = after.combo().stepIndex();

        if (afterStep != beforeStep) {
            effects.addAll(
                    adapter.onAction(
                            player,
                            ActionKind.COMBO_STEP,
                            afterStep,
                            null,
                            now
                    )
            );
        }

        ActionKind beforeLock = before.lock().actionKind();
        ActionKind afterLock = after.lock().actionKind();

        if (afterLock != beforeLock) {
            effects.addAll(
                    adapter.onAction(
                            player,
                            afterLock,
                            after.combo().stepIndex(),
                            after.lock().skillId(),
                            now
                    )
            );
        }

        return new Update(after, List.copyOf(effects));
    }

    public record Update(
            ServerCombatState state,
            List<RpgEffect> effects
    ) {}
}