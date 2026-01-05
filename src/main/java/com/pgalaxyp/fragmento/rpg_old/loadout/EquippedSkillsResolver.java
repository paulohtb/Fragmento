package com.pgalaxyp.fragmento.rpg_old.loadout;

import com.pgalaxyp.fragmento.rpg_old.catalyst.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.rpg_old.registry.RpgRegistry;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.EquippedSkillsState;
import com.pgalaxyp.fragmento.rpg_old.state.runtime.LoadoutState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class EquippedSkillsResolver {

    public EquippedSkillsState forPlayer(ServerPlayer player, LoadoutState loadout) {
        if (player == null || loadout == null || !loadout.valid()) {
            return EquippedSkillsState.empty();
        }

        ItemStack main = player.getItemBySlot(EquipmentSlot.MAINHAND);
        CatalystDefinition def = RpgRegistry.catalysts().resolve(main);
        if (def == null) {
            return EquippedSkillsState.empty();
        }

        return new EquippedSkillsState(def.skillsBySlot());
    }
}