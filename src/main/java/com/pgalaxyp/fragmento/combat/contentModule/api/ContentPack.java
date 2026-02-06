package com.pgalaxyp.fragmento.combat.contentModule.api;

import com.pgalaxyp.fragmento.combat.abilityModule.api.*;
import com.pgalaxyp.fragmento.combat.classModule.api.ClassKit;
import java.util.*;

public interface ContentPack {
    Collection<AbilityDefinition> abilities();
    Collection<ClassKit> classKits();
    Map<AbilityId, AbilityTriggerSpec> abilityTriggers();
}