package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.engine.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.combat.engine.registry.FragmentoCombatRegistries;
import com.pgalaxyp.fragmento.combat.state.runtime.EquippedSkillsRuntimeState;
import com.pgalaxyp.fragmento.combat.state.runtime.LoadoutRuntimeState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class EquippedSkillsAssigner {

    public EquippedSkillsRuntimeState forPlayer(ServerPlayer player, LoadoutRuntimeState loadout) {
        if (player == null || loadout == null || !loadout.valid()) {
            return EquippedSkillsRuntimeState.empty();
        }

        ItemStack main = player.getItemBySlot(EquipmentSlot.MAINHAND);
        CatalystDefinition def = FragmentoCombatRegistries.catalysts().resolve(main);
        if (def == null) {
            return EquippedSkillsRuntimeState.empty();
        }

        return new EquippedSkillsRuntimeState(def.skillsBySlot());
    }
}