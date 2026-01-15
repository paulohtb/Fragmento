package com.pgalaxyp.fragmento.combat.input.system;

import com.pgalaxyp.fragmento.combat.input.api.*;

public final class InputConsumptionPolicy {

    public boolean shouldBlockVanilla(InputContext context, SemanticInput input) {
        if (context == null || input == null) {
            throw new IllegalArgumentException();
        }
        return context.hasWeaponInHand();
    }
}