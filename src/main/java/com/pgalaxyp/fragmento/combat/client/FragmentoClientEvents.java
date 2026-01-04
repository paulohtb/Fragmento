package com.pgalaxyp.fragmento.combat.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pgalaxyp.fragmento.bootstrap.FragmentoMod;
import com.pgalaxyp.fragmento.combat.client.input.ClientCombatInputController;
import com.pgalaxyp.fragmento.combat.client.network.CombatIntentSender;
import com.pgalaxyp.fragmento.combat.client.proxy.CombatClientProxy;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = FragmentoMod.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public final class FragmentoClientEvents {

    private static final ClientCombatInputController INPUT =
            new ClientCombatInputController(new CombatIntentSender());

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        INPUT.clientTick();
    }

    @SubscribeEvent
    public static void onMouse(InputEvent.MouseButton.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (!inGame(mc)) {
            return;
        }

        if (!catalystActive()) {
            return;
        }

        if (event.getAction() != GLFW.GLFW_PRESS) {
            return;
        }

        int button = event.getButton();

        if (matchesMouseKey(mc.options.keyAttack, button)) {
            INPUT.onAttackClick();
            return;
        }

        if (matchesMouseKey(mc.options.keyUse, button)) {
            INPUT.onUseItemClick();
        }
    }

    private static boolean inGame(Minecraft mc) {
        return mc != null && mc.player != null && mc.options != null && mc.screen == null;
    }

    private static boolean matchesMouseKey(KeyMapping mapping, int mouseButton) {
        if (mapping == null) {
            return false;
        }
        InputConstants.Key key = mapping.getKey();
        return key != null
                && key.getType() == InputConstants.Type.MOUSE
                && key.getValue() == mouseButton;
    }

    private static boolean catalystActive() {
        var snap = CombatClientProxy.state().current();
        if (snap == null) return false;
        var loadout = snap.loadout();
        if (loadout == null) return false;
        return loadout.equippedCatalyst() != null
                && loadout.family() != null
                && loadout.offhandEmpty();
    }

    private FragmentoClientEvents() {}
}