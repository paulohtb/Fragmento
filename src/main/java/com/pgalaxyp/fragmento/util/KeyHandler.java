package com.pgalaxyp.fragmento.util;

import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public class KeyHandler {

    public static final KeyMapping CHANGE_WEAPON_KEY = new KeyMapping(
            "key.fragmento.change_weapon",
            GLFW.GLFW_KEY_TAB,
            "key.categories.fragmento"
    );

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(CHANGE_WEAPON_KEY);
    }
}