package com.pgalaxyp.fragmento.combat.contentModule.bard;

import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackDefinition;
import com.pgalaxyp.fragmento.combat.basicAttackModule.api.BasicAttackStepDefinition;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassKit;
import com.pgalaxyp.fragmento.combat.contentModule.api.ContentPack;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageElement;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageType;
import com.pgalaxyp.fragmento.combat.projectileModule.api.ProjectileSpec;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingFallback;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingMode;
import com.pgalaxyp.fragmento.combat.targetingModule.api.TargetingSpec;
import com.pgalaxyp.fragmento.combat.weaponModule.api.WeaponId;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum BardContentPack implements ContentPack {
    INSTANCE;

    private static final TargetingSpec SPEC = new TargetingSpec(TargetingMode.RAYCAST_SINGLE, 3.0, 16.0, 10.0, TargetingFallback.IMAGINARY_POINT);
    private static final DamageSpec D1 = new DamageSpec(1, DamageType.MAGIC, DamageElement.AIR);
    private static final DamageSpec D2 = new DamageSpec(2, DamageType.MAGIC, DamageElement.AIR);

    private static final BasicAttackDefinition FLUTE_BASIC = new BasicAttackDefinition(
            List.of(
                    new BasicAttackStepDefinition(SPEC, D1, BardIds.FLUTE_NOTE_PROJECTILE),
                    new BasicAttackStepDefinition(SPEC, D1, BardIds.FLUTE_NOTE_PROJECTILE),
                    new BasicAttackStepDefinition(SPEC, D2, BardIds.FLUTE_NOTE_PROJECTILE)
            ),
            20
    );

    private static final ProjectileSpec FLUTE_PROJECTILE = new ProjectileSpec(BardIds.FLUTE_NOTE_PROJECTILE, "minecraft:snowball", 1.5, 0.0f, true);

    private static final ClassKit KIT = new ClassKit(BardIds.BARD, Set.of(BardIds.FLUTE), Set.of(), Map.of());

    @Override public Collection<ClassKit> classKits() {
        return List.of(KIT);
    }

    @Override public Map<WeaponId, BasicAttackDefinition> weaponBasicAttacks() {
        return Map.of(BardIds.FLUTE, FLUTE_BASIC);
    }

    @Override public Collection<ProjectileSpec> projectiles() {
        return List.of(FLUTE_PROJECTILE);
    }
}
