package com.pgalaxyp.fragmento.combat.engine.runtime;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.combat.domain.id.CatalystId;
import com.pgalaxyp.fragmento.combat.engine.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.combat.engine.registry.FragmentoCombatRegistries;
import com.pgalaxyp.fragmento.combat.state.runtime.LoadoutRuntimeState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
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

        if (off != null && !off.isEmpty()) {
            return LoadoutRuntimeState.empty();
        }

        CatalystDefinition def = FragmentoCombatRegistries.catalysts().resolve(main);
        if (def == null) {
            return LoadoutRuntimeState.empty();
        }

        CatalystId catalystId = deriveStableId(main);
        CatalystFamilyId family = def.family();

        if (catalystId == null || family == null) {
            return LoadoutRuntimeState.empty();
        }

        return new LoadoutRuntimeState(catalystId, family, true);
    }

    private static CatalystId deriveStableId(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (key == null) {
            return null;
        }
        int raw = key.toString().hashCode();
        int id = Math.floorMod(raw, Integer.MAX_VALUE);
        return new CatalystId(id);
    }
}