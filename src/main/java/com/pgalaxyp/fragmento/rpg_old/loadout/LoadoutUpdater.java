package com.pgalaxyp.fragmento.rpg_old.loadout;

import com.pgalaxyp.fragmento.rpg_old.state.runtime.EquippedSkillsState;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.LoadoutState;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;

public final class LoadoutUpdater {

    private final LoadoutResolver resolver = new LoadoutResolver();
    private final EquippedSkillsResolver skills = new EquippedSkillsResolver();

    public ServerCombatState update(ServerCombatState state, ServerPlayer player) {
        if (state == null || player == null) {
            return state;
        }

        LoadoutState next = resolver.resolve(player);
        if (next != null && next.equals(state.loadout())) {
            return state;
        }

        ServerCombatState out = state.withLoadout(next);

        EquippedSkillsState eq = skills.forPlayer(player, out.loadout());
        if (eq != null && !eq.equals(out.equippedSkills())) {
            out = out.withEquippedSkills(eq);
        }

        return out;
    }
}