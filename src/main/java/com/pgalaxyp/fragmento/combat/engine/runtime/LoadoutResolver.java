package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.combat.domain.id.CatalystId;
import com.pgalaxyp.fragmento.combat.state.runtime.LoadoutRuntimeState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class LoadoutResolver {

    public LoadoutRuntimeState resolve(ServerPlayer player) {
        if (player == null) {
            return LoadoutRuntimeState.empty();
        }

        ItemStack main = player.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack off = player.getItemBySlot(EquipmentSlot.OFFHAND);

        if (!off.isEmpty()) {
            return LoadoutRuntimeState.empty();
        }

        CatalystId catalyst = extractCatalyst(main);
        CatalystFamilyId family = extractFamily(main);

        if (catalyst == null || family == null) {
            return LoadoutRuntimeState.empty();
        }

        return new LoadoutRuntimeState(catalyst, family, true);
    }

    private CatalystId extractCatalyst(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return new CatalystId(stack.getItem().hashCode());
    }

    private CatalystFamilyId extractFamily(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        return new CatalystFamilyId(stack.getItem().toString());
    }
}