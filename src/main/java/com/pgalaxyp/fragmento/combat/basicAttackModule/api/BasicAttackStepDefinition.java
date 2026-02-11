package com.pgalaxyp.fragmento.combat.basicAttackModule.api;

import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileId;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingSpec;
import java.util.Objects;

public record BasicAttackStepDefinition(TargetingSpec targeting, DamageSpec damage, ProjectileId projectileId) {
    public BasicAttackStepDefinition {
        Objects.requireNonNull(targeting);
        Objects.requireNonNull(damage);
        Objects.requireNonNull(projectileId);
    }
}
