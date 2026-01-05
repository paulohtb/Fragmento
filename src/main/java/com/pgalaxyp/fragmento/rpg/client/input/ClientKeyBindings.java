package com.pgalaxyp.fragmento.rpg.client.input;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class ClientKeyBindings {

    public static final KeyMapping NORMAL_SKILL =
            new KeyMapping(
                    "key.fragmento.normal_skill",
                    GLFW.GLFW_KEY_R,
                    "key.categories.fragmento"
            );

    public static final KeyMapping SPECIAL_SKILL =
            new KeyMapping(
                    "key.fragmento.special_skill",
                    GLFW.GLFW_KEY_G,
                    "key.categories.fragmento"
            );

    private ClientKeyBindings() {}
}