package com.pgalaxyp.fragmento.combat.domain.skill;

import java.util.HashMap;
import java.util.Map;

public final class SkillCatalog {

    private final Map<Integer, SkillDefinition> byId = new HashMap<>();

    public void register(SkillDefinition def) {
        if (def == null) {
            return;
        }
        byId.put(def.id().value(), def);
    }

    public SkillDefinition get(int id) {
        return byId.get(id);
    }
}