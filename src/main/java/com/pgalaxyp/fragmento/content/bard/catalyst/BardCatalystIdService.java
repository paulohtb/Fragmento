package com.pgalaxyp.fragmento.content.bard.catalyst;

import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class BardCatalystIdService {

    private BardCatalystIdService() {
    }

    public static UUID get(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;

        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (data.isEmpty()) return null;

        var tag = data.copyTag();
        if (!tag.hasUUID(BardInstrumentConstants.NBT_INSTRUMENT_INSTANCE_ID)) return null;

        return tag.getUUID(BardInstrumentConstants.NBT_INSTRUMENT_INSTANCE_ID);
    }

    public static UUID getOrCreate(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return null;

        UUID existing = get(stack);
        if (existing != null) return existing;

        UUID created = UUID.randomUUID();
        CustomData.update(
                DataComponents.CUSTOM_DATA,
                stack,
                tag -> tag.putUUID(BardInstrumentConstants.NBT_INSTRUMENT_INSTANCE_ID, created)
        );
        return created;
    }

    public static ItemStack findInPlayerInventory(ServerPlayer player, UUID instanceId) {
        if (player == null || instanceId == null) return ItemStack.EMPTY;

        var inv = player.getInventory();
        int size = inv.getContainerSize();

        for (int i = 0; i < size; i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.isEmpty()) continue;

            UUID id = get(stack);
            if (instanceId.equals(id)) {
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }
}
