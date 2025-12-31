package com.pgalaxyp.fragmento.combat.content.registry;

import com.pgalaxyp.fragmento.combat.domain.skill.SkillDefinition;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record SkillCatalog(Map<Integer, SkillDefinition> byId) {

    public SkillCatalog {
        byId = Map.copyOf(Objects.requireNonNullElseGet(byId, Map::of));
    }

    public SkillDefinition get(int id) {
        return byId.get(id);
    }

    public boolean contains(int id) {
        return byId.containsKey(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<Integer, SkillDefinition> map = new HashMap<>();

        public Builder put(SkillDefinition def) {
            if (def == null) {
                return this;
            }
            map.put(def.id().value(), def);
            return this;
        }

        public SkillCatalog build() {
            return new SkillCatalog(map);
        }
    }
}