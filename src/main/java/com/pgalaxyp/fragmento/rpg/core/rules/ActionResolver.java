package com.pgalaxyp.fragmento.rpg.core.rules;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.def.WeaponDef;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActionId;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;

import java.util.Optional;

public final class ActionResolver {

    public Optional<ActionId> resolvePrimaryAction(ActorState actor, RpgContent content) {
        if (actor == null || content == null) {
            throw new IllegalArgumentException();
        }

        if (actor.combo().isPresent()) {
            return Optional.of(actor.combo().get().actionId());
        }

        if (actor.equippedWeaponId().isEmpty()) {
            return Optional.empty();
        }

        WeaponDef weapon = content.weapon(actor.equippedWeaponId().get());
        return Optional.of(weapon.actionId());
    }
}