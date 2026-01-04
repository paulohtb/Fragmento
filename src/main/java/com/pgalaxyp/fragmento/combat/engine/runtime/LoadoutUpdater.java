package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.state.runtime.EquippedSkillsRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.LoadoutRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;

public final class LoadoutUpdater {

    private final LoadoutResolver resolver = new LoadoutResolver();
    private final EquippedSkillsAssigner skills = new EquippedSkillsAssigner();

    public ServerCombatState update(ServerCombatState state, ServerPlayer player) {
        if (state == null || player == null) {
            return state;
        }

        LoadoutRuntimeState next = resolver.resolve(player);
        if (next.equals(state.loadout())) {
            return state;
        }

        ServerCombatState out = state.withLoadout(next);

        EquippedSkillsRuntimeState eq = skills.forPlayer(player, next);
        if (!eq.equals(out.equippedSkills())) {
            out = out.withEquippedSkills(eq);
        }

        return out;
    }
}