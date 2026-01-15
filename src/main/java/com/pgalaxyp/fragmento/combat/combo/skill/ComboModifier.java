package com.pgalaxyp.fragmento.combat.combo.skill;

import com.pgalaxyp.fragmento.combat.combo.model.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;

public interface ComboModifier {

    boolean applies(ActorId actorId, WeaponId weaponId);

    ComboPattern modify(ActorId actorId, WeaponId weaponId, ComboPattern base);
}