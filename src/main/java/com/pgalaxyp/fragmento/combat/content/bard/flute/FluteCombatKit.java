package com.pgalaxyp.fragmento.combat.content.bard.flute;

import com.pgalaxyp.fragmento.combat.content.bard.flute.skill.FluteVortexInfusedSkill;
import com.pgalaxyp.fragmento.combat.content.registry.SkillCatalog;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.domain.skill.CombatSkillKind;
import com.pgalaxyp.fragmento.combat.domain.skill.SkillCategory;
import com.pgalaxyp.fragmento.combat.domain.skill.SkillDefinition;
import com.pgalaxyp.fragmento.combat.domain.skill.SkillId;
import com.pgalaxyp.fragmento.combat.rule.port.InfusedSkillResolver;
import java.util.Map;

public record FluteCombatKit(
        ComboDefinition combo,
        SkillCatalog skillCatalog,
        InfusedSkillResolver infusedSkillResolver
) {
    public static FluteCombatKit create(int ticksPerSecond) {
        ComboDefinition combo = FluteComboFactory.create(ticksPerSecond);

        SkillCatalog catalog = SkillCatalog.builder()
                .put(new SkillDefinition(
                        new SkillId(FluteSkillIds.VORTEX_INFUSION),
                        SkillCategory.COMBAT,
                        CombatSkillKind.INFUSED
                ))
                .build();

        var vortex = new FluteVortexInfusedSkill(ticksPerSecond);
        InfusedSkillResolver resolver = new FluteInfusedSkillResolver(
                Map.of(FluteSkillIds.VORTEX_INFUSION, vortex)
        );

        return new FluteCombatKit(combo, catalog, resolver);
    }
}