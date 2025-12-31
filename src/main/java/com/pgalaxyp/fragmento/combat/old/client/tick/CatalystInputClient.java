//package com.pgalaxyp.fragmento.combat.old.client.tick;
//
//import com.pgalaxyp.fragmento.combat.old.client.data.ClientSkillState;
//import com.pgalaxyp.fragmento.combat.old.client.input.BardSkillClientController;
//import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystItem;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.player.LocalPlayer;
//import net.minecraft.world.item.ItemStack;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.bus.api.SubscribeEvent;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.neoforge.client.event.ClientTickEvent;
//
//@EventBusSubscriber(modid = "fragmento", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
//public final class CatalystInputClient {
//
//    private CatalystInputClient() {
//    }
//
//    @SubscribeEvent
//    public static void tick(ClientTickEvent.Pre event) {
//        Minecraft mc = Minecraft.getInstance();
//        LocalPlayer player = mc.player;
//
//        if (mc.level == null || player == null) {
//            BardSkillClientController.reset();
//            ClientSkillState.clear();
//            return;
//        }
//
//        ItemStack stack = player.getMainHandItem();
//        BardCatalystItem instrument = stack.getItem() instanceof BardCatalystItem i ? i : null;
//
//        BardSkillClientController.tick(mc, player, stack, instrument);
//    }
//}