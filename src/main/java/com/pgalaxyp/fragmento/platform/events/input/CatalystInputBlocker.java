package com.pgalaxyp.fragmento.platform.events.input;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(
        modid = "fragmento",
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.GAME
)
public final class CatalystInputBlocker {

    private CatalystInputBlocker() {
    }

    @SubscribeEvent
    public static void onInput(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        ItemStack stack = mc.player.getMainHandItem();
        if (!(stack.getItem() instanceof BardCatalystItem)) return;

        if (event.isAttack() || event.isUseItem()) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }
}
