package com.pgalaxyp.fragmento.combat.engine.restriction;

import com.pgalaxyp.fragmento.combat.engine.runtime.ServerCombatSystem;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

public final class ItemSwapBlocker {

    @SubscribeEvent
    public void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        if (event.getSlot() != EquipmentSlot.OFFHAND) {
            return;
        }

        ServerCombatState state = ServerCombatSystem.get().stateFor(player);
        if (state == null) {
            return;
        }

        if (!state.lock().active(ServerCombatSystem.get().now(player))) {
            return;
        }

        player.setItemSlot(EquipmentSlot.OFFHAND, event.getFrom());
    }
}