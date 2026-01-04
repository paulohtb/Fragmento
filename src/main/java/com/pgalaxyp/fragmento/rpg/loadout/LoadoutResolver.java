package com.pgalaxyp.fragmento.rpg.loadout;

import com.pgalaxyp.fragmento.rpg.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg.domain.id.CatalystId;
import com.pgalaxyp.fragmento.rpg.catalyst.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.rpg.registry.RpgRegistry;
import com.pgalaxyp.fragmento.rpg.state.runtime.LoadoutState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public final class LoadoutResolver {

    public LoadoutState resolve(ServerPlayer player) {
        if (player == null) {
            return LoadoutState.empty();
        }

        ItemStack main = player.getItemBySlot(EquipmentSlot.MAINHAND);
        ItemStack off = player.getItemBySlot(EquipmentSlot.OFFHAND);

        if (off != null && !off.isEmpty()) {
            return LoadoutState.empty();
        }

        CatalystDefinition def = RpgRegistry.catalysts().resolve(main);
        if (def == null) {
            return LoadoutState.empty();
        }

        CatalystId catalystId = deriveStableId(main);
        CatalystFamilyId family = def.family();

        if (catalystId == null || family == null) {
            return LoadoutState.empty();
        }

        return new LoadoutState(catalystId, family, true);
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