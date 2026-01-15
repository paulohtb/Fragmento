package com.pgalaxyp.fragmento.combat.combo.skill;

import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import java.util.*;

public final class ComboSkillResolver {

    private final List<ComboModifier> modifiers;

    public ComboSkillResolver(List<ComboModifier> modifiers) {
        if (modifiers == null) {
            throw new IllegalArgumentException();
        }

        this.modifiers = List.copyOf(modifiers);
        for (ComboModifier m : this.modifiers) {
            if (m == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    public ComboPattern resolve(ActorId actorId, WeaponId weaponId, ComboPattern base) {
        Objects.requireNonNull(actorId);
        Objects.requireNonNull(weaponId);
        Objects.requireNonNull(base);

        ComboPattern out = base;
        for (ComboModifier m : modifiers) {
            if (m.applies(actorId, weaponId)) {
                ComboPattern next = m.modify(actorId, weaponId, out);
                if (next == null) {
                    throw new IllegalArgumentException();
                }
                out = next;
            }
        }

        return out;
    }
}