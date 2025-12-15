package com.pgalaxyp.fragmento.platform.events.input;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.content.bard.skill.BardSkillClientController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(
        modid = "fragmento",
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.GAME
)
public final class CatalystInputClient {

    @SubscribeEvent
    public static void tick(ClientTickEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (mc.level == null || player == null) {
            BardSkillClientController.resetClientState();
            return;
        }

        ItemStack stack = player.getMainHandItem();
        BardCatalystItem instrument = stack.getItem() instanceof BardCatalystItem i ? i : null;

        BardSkillClientController.tick(mc, player, stack, instrument);
    }
}
