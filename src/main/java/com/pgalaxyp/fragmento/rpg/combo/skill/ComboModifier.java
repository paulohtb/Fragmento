package com.pgalaxyp.fragmento.rpg.combo.skill;

import com.pgalaxyp.fragmento.rpg.combo.model.*;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.*;

public interface ComboModifier {

    boolean applies(ActorId actorId, WeaponId weaponId);

    ComboPattern modify(ActorId actorId, WeaponId weaponId, ComboPattern base);
}