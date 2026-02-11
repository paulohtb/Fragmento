package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityDefinition;
import com.pgalaxyp.fragmento.combat.abilityModule.api.AbilityId;
import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackDefinition;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassKit;
import com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileSpec;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface ContentPack {
    default Collection<AbilityDefinition> abilities() { return List.of(); }
    default Collection<ClassKit> classKits() { return List.of(); }
    default Map<AbilityId, AbilityTriggerSpec> abilityTriggers() { return Map.of(); }

    default Map<WeaponId, BasicAttackDefinition> weaponBasicAttacks() { return Map.of(); }
    default Collection<ProjectileSpec> projectiles() { return List.of(); }
}
