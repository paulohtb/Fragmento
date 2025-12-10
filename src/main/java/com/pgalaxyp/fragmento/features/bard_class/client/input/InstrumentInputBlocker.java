package com.pgalaxyp.fragmento.features.bard_class.client.input;

import com.pgalaxyp.fragmento.features.bard_class.instrument.InstrumentBase;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class InstrumentInputBlocker {

    private InstrumentInputBlocker() {}

    @SubscribeEvent
    public static void onClick(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStack stack = mc.player.getMainHandItem();
        if (!(stack.getItem() instanceof InstrumentBase)) return;

        if (event.isAttack()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }

        if (event.isUseItem()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }
}
