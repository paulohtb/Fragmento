package com.pgalaxyp.fragmento.rpg.client.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.rpg.client.ClientContext;
import com.pgalaxyp.fragmento.rpg.client.input.ClientInputController;
import com.pgalaxyp.fragmento.rpg.client.network.ClientIntentSender;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(
        modid = FragmentoMod.MODID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.GAME
)
public final class ClientGameplayEvents {

    private static final ClientInputController INPUT =
            new ClientInputController(new ClientIntentSender());

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        INPUT.clientTick();
    }

    @SubscribeEvent
    public static void onInteractionKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft mc = Minecraft.getInstance();
        if (!ClientContext.inGame(mc)) return;
        if (!ClientContext.catalystActive(mc)) return;

        KeyMapping mapping = event.getKeyMapping();
        if (mapping == null) return;

        if (mapping == mc.options.keyAttack || mapping == mc.options.keyUse) {
            event.setSwingHand(false);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMouse(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (!ClientContext.inGame(mc)) return;
        if (!ClientContext.catalystActive(mc)) return;
        if (event.getAction() != GLFW.GLFW_PRESS) return;

        int button = event.getButton();

        if (matchesMouseKey(mc.options.keyAttack, button)) {
            event.setCanceled(true);
            INPUT.onAttackClick();
            return;
        }

        if (matchesMouseKey(mc.options.keyUse, button)) {
            event.setCanceled(true);
            INPUT.onUseItemClick();
        }
    }

    private static boolean matchesMouseKey(KeyMapping mapping, int mouseButton) {
        if (mapping == null) return false;
        InputConstants.Key key = mapping.getKey();
        return key != null
                && key.getType() == InputConstants.Type.MOUSE
                && key.getValue() == mouseButton;
    }

    private ClientGameplayEvents() {}
}