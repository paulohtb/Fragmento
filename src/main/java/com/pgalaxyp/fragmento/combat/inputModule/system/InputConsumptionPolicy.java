package com.pgalaxyp.fragmento.combat.inputModule.system;

import com.pgalaxyp.fragmento.combat.inputModule.api.*;

public final class InputConsumptionPolicy {

    public boolean shouldBlockVanilla(InputContext context, SemanticInput input) {
        if (context == null || input == null) {
            throw new IllegalArgumentException();
        }
        return context.hasWeaponInHand();
    }
}