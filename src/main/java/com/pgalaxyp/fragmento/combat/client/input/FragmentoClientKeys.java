package com.pgalaxyp.fragmento.combat.client.input;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public final class FragmentoClientKeys {

    public static final KeyMapping NORMAL_SKILL =
            new KeyMapping(
                    "key.fragmento.normal_skill",
                    GLFW.GLFW_KEY_R,
                    "key.categories.fragmento"
            );

    private FragmentoClientKeys() {}
}