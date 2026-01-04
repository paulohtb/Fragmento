package com.pgalaxyp.fragmento.combat.engine.registry;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.combat.engine.profile.CombatProfile;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public final class CatalystRegistry {

    private final List<CatalystDefinition> definitions = new ArrayList<>();

    public void register(CatalystDefinition def) {
        if (def == null) {
            return;
        }
        definitions.add(def);
    }

    public CatalystDefinition resolve(ItemStack mainHand) {
        if (mainHand == null || mainHand.isEmpty()) {
            return null;
        }
        for (CatalystDefinition def : definitions) {
            if (def != null && def.matches(mainHand)) {
                return def;
            }
        }
        return null;
    }

    public CombatProfile profileFor(CatalystFamilyId family) {
        if (family == null) {
            return CombatProfile.NOOP;
        }
        for (CatalystDefinition def : definitions) {
            if (def != null && family.equals(def.family())) {
                return def.profile();
            }
        }
        return CombatProfile.NOOP;
    }
}