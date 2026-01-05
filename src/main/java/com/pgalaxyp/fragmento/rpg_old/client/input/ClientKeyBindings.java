package com.pgalaxyp.fragmento.rpg_old.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class ClientKeyBindings {

    public static final KeyMapping NORMAL_SKILL =
            new KeyMapping(
                    "key.fragmento.normal",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_Q,
                    "key.categories.fragmento"
            );

    public static final KeyMapping SPECIAL_SKILL =
            new KeyMapping(
                    "key.fragmento.special",
                    InputConstants.Type.MOUSE,
                    GLFW.GLFW_MOUSE_BUTTON_RIGHT,
                    "key.categories.fragmento"
            );

    private ClientKeyBindings() {}
}