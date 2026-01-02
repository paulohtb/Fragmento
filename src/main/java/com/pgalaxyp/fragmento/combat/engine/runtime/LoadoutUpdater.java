package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.state.runtime.LoadoutRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;

public final class LoadoutUpdater {

    private final LoadoutResolver resolver = new LoadoutResolver();

    public ServerCombatState update(ServerCombatState state, ServerPlayer player) {
        if (state == null || player == null) {
            return state;
        }

        LoadoutRuntimeState next = resolver.resolve(player);
        if (next.equals(state.loadout())) {
            return state;
        }

        return state.withLoadout(next);
    }
}