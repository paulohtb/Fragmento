package com.pgalaxyp.fragmento.feature.bard.client.input;

import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

public final class BardKeyMappings {

    public static KeyMapping BARD_WEAPON_CHARGED;

    private BardKeyMappings() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        BARD_WEAPON_CHARGED = new KeyMapping(
                "key.fragmento.bard_weapon_charged",
                GLFW.GLFW_KEY_R,
                "key.categories.fragmento"
        );
        event.register(BARD_WEAPON_CHARGED);
    }
}
