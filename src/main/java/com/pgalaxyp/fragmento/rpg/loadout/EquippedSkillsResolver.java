package com.pgalaxyp.fragmento.rpg.loadout;

import com.pgalaxyp.fragmento.rpg.catalyst.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.rpg.registry.RpgRegistry;
import com.pgalaxyp.fragmento.rpg.state.runtime.EquippedSkillsState;
import com.pgalaxyp.fragmento.rpg.state.runtime.LoadoutState;
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