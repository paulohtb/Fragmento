package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;

public record ComboCompleted(
        ActorId actorId,
        WeaponId weaponId
) implements DomainEvent {}