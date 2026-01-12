package com.pgalaxyp.fragmento.rpg.input.system;

import com.pgalaxyp.fragmento.rpg.input.api.ModInputContext;
import com.pgalaxyp.fragmento.rpg.input.api.ModInputIntent;

public final class InputConsumptionPolicy {

    private final WeaponInputPolicy weaponPolicy;

    public InputConsumptionPolicy(WeaponInputPolicy weaponPolicy) {
        if (weaponPolicy == null) {
            throw new IllegalArgumentException();
        }
        this.weaponPolicy = weaponPolicy;
    }

    public boolean consumeVanilla(ModInputContext context, ModInputIntent intent) {
        if (context == null || intent == null) {
            throw new IllegalArgumentException();
        }

        if (!context.hasWeaponInHand()) {
            return false;
        }

        WeaponInputPolicy.WeaponInputRule rule = weaponPolicy.resolve(context);

        if (!rule.dominateInput()) {
            return false;
        }

        return true;
    }
}