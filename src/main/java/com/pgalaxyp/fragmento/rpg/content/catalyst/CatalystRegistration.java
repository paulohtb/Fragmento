package com.pgalaxyp.fragmento.rpg.content.catalyst;

import com.pgalaxyp.fragmento.rpg.content.skill.BardSkills;
import com.pgalaxyp.fragmento.rpg.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg.catalyst.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.rpg.registry.RpgRegistry;

import java.util.Map;

public final class CatalystRegistration {

    private static final SkillSlotId NORMAL = new SkillSlotId(1);
    private static final SkillSlotId SPECIAL = new SkillSlotId(2);

    public static void registerAll() {
        Map<SkillSlotId, SkillId> skills = Map.of(
                NORMAL, BardSkills.BARDO_NORMAL_INFUSED,
                SPECIAL, BardSkills.BARDO_SPECIAL_CASTED
        );

        RpgRegistry.catalysts().register(
                new CatalystDefinition(
                        new CatalystFamilyId("bardo"),
                        FluteItem::isFlute,
                        skills
                )
        );
    }

    private CatalystRegistration() {}
}