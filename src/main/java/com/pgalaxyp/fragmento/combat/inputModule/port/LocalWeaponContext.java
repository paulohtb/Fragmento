package com.pgalaxyp.fragmento.combat.inputModule.port;

import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Optional;

public interface LocalWeaponContext { Optional<WeaponId> weaponInMainHandId(); }