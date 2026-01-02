package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.timing.CombatTime;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;

public final class RuntimeLoadoutSystem {

    private final LoadoutUpdater updater = new LoadoutUpdater();

    public ServerCombatState tick(
            ServerCombatState state,
            ServerPlayer player,
            CombatTime now
    ) {
        return updater.update(state, player);
    }
}