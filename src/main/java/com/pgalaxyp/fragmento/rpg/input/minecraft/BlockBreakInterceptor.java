package com.pgalaxyp.fragmento.rpg.input.minecraft;

import com.pgalaxyp.fragmento.rpg.input.api.ModInputController;

public final class BlockBreakInterceptor {

    private final ModInputController controller;

    public BlockBreakInterceptor(ModInputController controller) {
        if (controller == null) {
            throw new IllegalArgumentException();
        }
        this.controller = controller;
    }
}