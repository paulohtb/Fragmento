package com.pgalaxyp.fragmento.client;

import com.pgalaxyp.fragmento.Fragmento;
import com.pgalaxyp.fragmento.item.bard_weapon.AbstractBardWeapon;
import com.pgalaxyp.fragmento.network.LeftClickPacket;
import com.pgalaxyp.fragmento.registry.ItensRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import static com.pgalaxyp.fragmento.util.KeyHandler.CHANGE_WEAPON_KEY;

@EventBusSubscriber(modid = Fragmento.MODID, value = Dist.CLIENT)
public class ClientEvents {

    private static boolean shouldSkip(Minecraft mc) {
        return mc.level == null || mc.player == null;
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (shouldSkip(mc)) return;

        LocalPlayer player = mc.player;
        processBardAttack(mc, player);
        processWeaponMenu(mc);
    }

    private static void processBardAttack(Minecraft mc, Player player) {
        if (mc.screen != null) return;
        if (!mc.options.keyAttack.isDown()) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof AbstractBardWeapon)) return;
        if (mc.getConnection() == null) return;

        boolean ultimateDown = mc.options.keyUse.isDown();
        mc.getConnection().send(new LeftClickPacket(InteractionHand.MAIN_HAND, ultimateDown));
    }

    @SubscribeEvent
    public static void onInteractionKeyMapping(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;
        if (mc.screen != null) return;
        if (event.getKeyMapping() != mc.options.keyAttack) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof AbstractBardWeapon)) return;

        boolean specialDown = mc.options.keyUse.isDown();

        if (specialDown) {
            event.setCanceled(true);
            event.setSwingHand(false);
        }
    }

    private static void processWeaponMenu(Minecraft mc) {
        if (!CHANGE_WEAPON_KEY.consumeClick()) return;
        if (mc.screen != null) return;
        if (!ChangeBardWeaponGUI.canOpen()) return;

        Item[] instruments = new Item[]{
                ItensRegistry.GUITAR.get(),
                ItensRegistry.FLUTE.get(),
                ItensRegistry.LUTE.get(),
                ItensRegistry.LYRE.get(),
                ItensRegistry.DRUM.get()
        };

        mc.setScreen(new ChangeBardWeaponGUI(instruments, ItensRegistry.LUTE.get()));
    }

    @SubscribeEvent
    public static void onKeyReleased(ScreenEvent.KeyReleased.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.screen instanceof ChangeBardWeaponGUI) {
            mc.setScreen(null);
        }
    }
}