package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import com.pgalaxyp.fragmento.combat.effectModule.api.EffectId;
import com.pgalaxyp.fragmento.combat.damageModule.api.DamageSpec;
import java.util.*;

public interface ContentPack {
    Collection<AbilityDefinition> abilities();
    Map<EffectId, DamageSpec> effects();
    List<AbilityRule> abilityRules();
    Map<WeaponId, AbilityId> primaryBindings();
    Map<AbilityId, AbilityTriggerSpec> abilityTriggers();
    default AbilityTuning abilityTuning() { return AbilityTuning.DEFAULT; }
}