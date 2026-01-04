package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.domain.action.ActionKind;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.engine.profile.CombatEffect;
import com.pgalaxyp.fragmento.combat.engine.profile.CombatProfile;
import com.pgalaxyp.fragmento.combat.rule.combat.AbilityEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.CombatEngine;
import com.pgalaxyp.fragmento.combat.rule.port.CombatClock;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class CombatSession {

    public interface ProfileResolver {
        CombatProfile resolve(ServerCombatState state);
    }

    private final CombatClock clock;
    private final CombatEngine combatEngine;
    private final AbilityEngine abilityEngine;
    private final RuntimeLoadoutSystem loadoutSystem;
    private final ProfileResolver profiles;

    private ServerCombatState state;

    public CombatSession(
            CombatClock clock,
            CombatEngine combatEngine,
            AbilityEngine abilityEngine,
            RuntimeLoadoutSystem loadoutSystem,
            ProfileResolver profiles,
            ServerCombatState initial
    ) {
        this.clock = clock;
        this.combatEngine = combatEngine;
        this.abilityEngine = abilityEngine;
        this.loadoutSystem = loadoutSystem;
        this.profiles = profiles;
        this.state = initial;
    }

    public Update onAttackIntent(ServerPlayer player, AttackIntent intent) {
        if (player == null || intent == null) {
            return Update.noChange(state);
        }

        CombatTime now = clock.now();

        ServerCombatState before = state;
        state = loadoutSystem.tick(state, player, now);
        state = combatEngine.applyAttack(state, intent, now);

        return collectTransitions(player, before, state, now);
    }

    public Update onAbilityIntent(ServerPlayer player, AbilityIntent intent) {
        if (player == null || intent == null) {
            return Update.noChange(state);
        }

        CombatTime now = clock.now();

        ServerCombatState before = state;
        state = loadoutSystem.tick(state, player, now);
        state = abilityEngine.apply(state, intent, now);

        return collectTransitions(player, before, state, now);
    }

    public Update tick(ServerPlayer player) {
        if (player == null) {
            return Update.noChange(state);
        }

        CombatTime now = clock.now();

        ServerCombatState before = state;

        state = loadoutSystem.tick(state, player, now);
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
            CombatTime now
    ) {
        if (before == null || after == null) {
            return Update.noChange(state);
        }

        List<CombatEffect> effects = new ArrayList<>();

        int beforeStep = before.combo() != null ? before.combo().stepIndex() : 0;
        int afterStep = after.combo() != null ? after.combo().stepIndex() : 0;

        ActionKind beforeLock = before.lock() != null ? before.lock().actionKind() : ActionKind.NONE;
        ActionKind afterLock = after.lock() != null ? after.lock().actionKind() : ActionKind.NONE;

        boolean changed = after.version() != null && before.version() != null
                && after.version().value() != before.version().value();

        if (afterStep != beforeStep) {
            CombatProfile profile = safeProfile(after);
            try {
                effects.addAll(profile.onComboStep(player, afterStep, now));
            } catch (Throwable t) {
                FragmentoLog.combatEx(
                        t,
                        "session profile onComboStep crashed, player.uuid={} step={} profile={}",
                        player.getUUID(),
                        afterStep,
                        profile.getClass().getName()
                );
            }
        }

        if (afterLock == ActionKind.INFUSED_EXECUTE && beforeLock != ActionKind.INFUSED_EXECUTE) {
            CombatProfile profile = safeProfile(after);
            try {
                effects.addAll(profile.onInfusedExecute(player, after.lock().skillId(), now));
            } catch (Throwable t) {
                FragmentoLog.combatEx(
                        t,
                        "session profile onInfusedExecute crashed, player.uuid={} skillId={} profile={}",
                        player.getUUID(),
                        after.lock().skillId() != null ? after.lock().skillId().value() : null,
                        profile.getClass().getName()
                );
            }
        }

        if (afterLock == ActionKind.CAST_FINISH && beforeLock != ActionKind.CAST_FINISH) {
            CombatProfile profile = safeProfile(after);
            try {
                effects.addAll(profile.onCastFinish(player, after.lock().skillId(), now));
            } catch (Throwable t) {
                FragmentoLog.combatEx(
                        t,
                        "session profile onCastFinish crashed, player.uuid={} skillId={} profile={}",
                        player.getUUID(),
                        after.lock().skillId() != null ? after.lock().skillId().value() : null,
                        profile.getClass().getName()
                );
            }
        }

        if (!changed && effects.isEmpty()) {
            return Update.noChange(after);
        }

        return new Update(after, List.copyOf(effects));
    }

    private CombatProfile safeProfile(ServerCombatState st) {
        if (profiles == null) {
            return CombatProfile.NOOP;
        }
        try {
            CombatProfile p = profiles.resolve(st);
            return p != null ? p : CombatProfile.NOOP;
        } catch (Throwable ignored) {
            return CombatProfile.NOOP;
        }
    }

    public record Update(
            ServerCombatState state,
            List<CombatEffect> effects
    ) {
        public static Update noChange(ServerCombatState state) {
            return new Update(state, List.of());
        }
    }
}