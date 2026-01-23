package com.pgalaxyp.fragmento.combat.content.api;

import com.pgalaxyp.fragmento.combat.ability.api.*;
import com.pgalaxyp.fragmento.combat.core.ids.WeaponId;
import com.pgalaxyp.fragmento.combat.skill.api.SkillRule;
import com.pgalaxyp.fragmento.combat.effect.api.EffectDef;
import java.util.*;

public interface ContentPack {
    Collection<AbilityDef> abilities();
    Collection<EffectDef> effects();
    List<SkillRule> skills();
    Map<WeaponId, AbilityId> primaryBindings();
}