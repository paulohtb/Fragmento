package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.input.api.*;

public final class InputConsumptionPolicy {

    public boolean shouldBlockVanilla(InputContext context, SemanticInput input) {
        if (context == null || input == null) {
            throw new IllegalArgumentException();
        }
        return context.hasWeaponInHand();
    }
}