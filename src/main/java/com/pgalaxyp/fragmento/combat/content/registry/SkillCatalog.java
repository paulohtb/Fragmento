package com.pgalaxyp.fragmento.combat.content.registry;

import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.skill.SkillDefinition;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public record SkillCatalog(Map<SkillId, SkillDefinition> byId) {

    public SkillCatalog {
        byId = Map.copyOf(Objects.requireNonNullElseGet(byId, Map::of));
    }

    public SkillDefinition get(SkillId id) {
        return id == null ? null : byId.get(id);
    }

    public boolean contains(SkillId id) {
        return id != null && byId.containsKey(id);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<SkillId, SkillDefinition> map = new HashMap<>();

        public Builder put(SkillDefinition def) {
            if (def == null || def.id() == null) {
                return this;
            }
            map.put(def.id(), def);
            return this;
        }

        public SkillCatalog build() {
            return new SkillCatalog(map);
        }
    }
}