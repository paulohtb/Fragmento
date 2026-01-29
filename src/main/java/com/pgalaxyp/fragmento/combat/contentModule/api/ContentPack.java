package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.effectModule.api.*;
import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.weaponModule.WeaponId;
import java.util.*;

public interface ContentPack {
    Collection<AbilityDefinition> abilities();
    Collection<EffectDef> effects();
    List<AbilityRule> abilityRules();
    Map<WeaponId, AbilityId> primaryBindings();
    Map<AbilityId, AbilityEffectSpec> abilityEffects();
}