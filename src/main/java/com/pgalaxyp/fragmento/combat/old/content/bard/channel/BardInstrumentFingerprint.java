package com.pgalaxyp.fragmento.combat.old.content.bard.channel;

import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.combat.old.system.channel.ChannelFingerprint;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class BardInstrumentFingerprint implements ChannelFingerprint {

    private final ResourceLocation itemId;
    private final UUID instanceId;

    public BardInstrumentFingerprint(ItemStack stack) {
        this.itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        this.instanceId = BardCatalystIdService.getOrCreate(stack);
    }

    @Override
    public boolean matches(ItemStack stack) {
        if (stack == null || stack.isEmpty()) return false;

        ResourceLocation currentItemId =
                BuiltInRegistries.ITEM.getKey(stack.getItem());

        if (!itemId.equals(currentItemId)) return false;

        UUID currentInstance = BardCatalystIdService.get(stack);
        return instanceId != null && instanceId.equals(currentInstance);
    }
}
