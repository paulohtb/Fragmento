package com.pgalaxyp.fragmento.NEW;

import com.pgalaxyp.fragmento.NEW.newnew.NewLuteTestWeaponInputPacket;
import com.pgalaxyp.fragmento.NEW.newnew.NewNewAbstractWeapon;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = "fragmento", bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class NewClientEvents {

    private static boolean leftMouseWasPressed = false;
    private static boolean rightMouseWasPressed = false;
    private static int normalAbilityClientCooldownTicks = 0;

    private static boolean canProcessAbilityInput(Minecraft mc) {
        return mc.player != null && mc.screen == null;
    }

    @SubscribeEvent
    public static void onNormalAbilityClickInput(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) {
            return;
        }

        if (event.getKeyMapping() != mc.options.keyAttack) {
            return;
        }

        if (!canProcessAbilityInput(mc)) {
            return;
        }

        event.setCanceled(true);
        event.setSwingHand(false);
    }

    @SubscribeEvent
    public static void onClientTickAfterGameLogic(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (!canProcessAbilityInput(mc)) {
            return;
        }

        var player = mc.player;

        boolean leftPressedRaw = mc.options.keyAttack.isDown();
        var stack = player.getMainHandItem();

        if (leftPressedRaw && stack.getItem() instanceof NewNewAbstractWeapon) {
            PacketDistributor.sendToServer(new NewLuteTestWeaponInputPacket());
            leftMouseWasPressed = leftPressedRaw;
            return;
        }

        var holdingAbilityWeapon = !stack.isEmpty() && stack.getItem() instanceof NewAbstractWeapon;

        boolean leftPressed = mc.options.keyAttack.isDown();
        if (!holdingAbilityWeapon) {
            leftPressed = false;
        }

        if (leftPressed && !leftMouseWasPressed) {
            normalAbilityClientCooldownTicks = 0;
        }

        if (normalAbilityClientCooldownTicks > 0) {
            normalAbilityClientCooldownTicks = normalAbilityClientCooldownTicks - 1;
        }

        if (leftPressed && normalAbilityClientCooldownTicks <= 0 && holdingAbilityWeapon) {
            PacketDistributor.sendToServer(new NormalAbilityInputPacket());
            normalAbilityClientCooldownTicks = NewAbstractWeapon.getNormalAbilityClientCooldownTicks();
        }

        leftMouseWasPressed = leftPressed;

        boolean rightPressed = holdingAbilityWeapon && mc.options.keyUse.isDown();

        if (rightPressed != rightMouseWasPressed) {
            PacketDistributor.sendToServer(new SpecialAbilityInputPacket(rightPressed));
        }

        rightMouseWasPressed = rightPressed;
    }
}
