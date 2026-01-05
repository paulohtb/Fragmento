package com.pgalaxyp.fragmento.rpg.session;

import com.pgalaxyp.fragmento.rpg.combat.engine.CombatEngine;
import com.pgalaxyp.fragmento.rpg.loadout.LoadoutUpdater;
import com.pgalaxyp.fragmento.rpg.skill.runtime.AbilityEngine;
import com.pgalaxyp.fragmento.rpg.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RpgSessionManager {

    private final Map<UUID, RpgSession> sessions = new HashMap<>();

    private final CombatEngine combatEngine;
    private final AbilityEngine abilityEngine;
    private final LoadoutUpdater loadoutUpdater = new LoadoutUpdater();

    public RpgSessionManager(
            CombatEngine combatEngine,
            AbilityEngine abilityEngine
    ) {
        this.combatEngine = combatEngine;
        this.abilityEngine = abilityEngine;
    }

    public RpgSession sessionFor(ServerPlayer player) {
        UUID id = player.getUUID();

        return sessions.computeIfAbsent(id, k ->
                new RpgSession(
                        player,
                        combatEngine,
                        abilityEngine,
                        loadoutUpdater,
                        ServerCombatState.initial(null)
                )
        );
    }

    public void clear(ServerPlayer player) {
        if (player != null) {
            sessions.remove(player.getUUID());
        }
    }
}