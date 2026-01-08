package com.pgalaxyp.fragmento.rpg.core.domain.event;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.WeaponId;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.EffectId;

public record ComboStepEmitted(
        ActorId actorId,
        WeaponId weaponId,
        int stepIndex,
        EffectId effectId
) implements DomainEvent {}