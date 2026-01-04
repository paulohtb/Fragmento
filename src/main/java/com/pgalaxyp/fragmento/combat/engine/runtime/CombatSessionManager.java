package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.engine.profile.CombatProfile;
import com.pgalaxyp.fragmento.combat.engine.registry.FragmentoCombatRegistries;
import com.pgalaxyp.fragmento.combat.engine.time.TickClock;
import com.pgalaxyp.fragmento.combat.engine.time.TickSource;
import com.pgalaxyp.fragmento.combat.rule.combat.AbilityEngine;
import com.pgalaxyp.fragmento.combat.rule.combat.CombatEngine;
import com.pgalaxyp.fragmento.combat.rule.port.CombatClock;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CombatSessionManager {

    private final Map<UUID, CombatSession> sessions = new HashMap<>();

    private final CombatEngine combatEngine;
    private final AbilityEngine abilityEngine;

    private final RuntimeLoadoutSystem loadoutSystem = new RuntimeLoadoutSystem();

    public CombatSessionManager(
            CombatEngine combatEngine,
            AbilityEngine abilityEngine
    ) {
        this.combatEngine = combatEngine;
        this.abilityEngine = abilityEngine;
    }

    public CombatSession sessionFor(ServerPlayer player) {
        UUID id = player.getUUID();

        CombatSession existing = sessions.get(id);
        if (existing != null) {
            return existing;
        }

        TickSource src = () -> player.level().getGameTime();
        CombatClock clock = new TickClock(src);

        CombatSession.ProfileResolver resolver = st -> {
            if (st == null || st.loadout() == null || st.loadout().family() == null) {
                return CombatProfile.NOOP;
            }
            return FragmentoCombatRegistries.catalysts().profileFor(st.loadout().family());
        };

        CombatSession created = new CombatSession(
                clock,
                combatEngine,
                abilityEngine,
                loadoutSystem,
                resolver,
                ServerCombatState.initial()
        );

        sessions.put(id, created);

        FragmentoLog.runtime(
                "session created, player.uuid={} sessions.size={}",
                id,
                sessions.size()
        );

        return created;
    }

    public void clear(ServerPlayer player) {
        if (player == null) {
            return;
        }
        sessions.remove(player.getUUID());
    }

    public int size() {
        return sessions.size();
    }
}