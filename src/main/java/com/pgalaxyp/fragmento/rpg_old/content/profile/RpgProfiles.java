package com.pgalaxyp.fragmento.rpg_old.content.profile;

import com.pgalaxyp.fragmento.rpg_old.catalyst.registry.CatalystDefinition;
import com.pgalaxyp.fragmento.rpg_old.content.catalyst.FluteItem;
import com.pgalaxyp.fragmento.rpg_old.content.skill.BardSkills;
import com.pgalaxyp.fragmento.rpg_old.domain.id.CatalystFamilyId;
import com.pgalaxyp.fragmento.rpg_old.domain.id.SkillId;
import com.pgalaxyp.fragmento.rpg_old.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg_old.engine.catalyst.BardFluteEffects;
import com.pgalaxyp.fragmento.rpg_old.registry.RpgRegistry;
import com.pgalaxyp.fragmento.rpg_old.skill.config.SkillTuning;
import com.pgalaxyp.fragmento.rpg_old.domain.timing.Duration;
import java.util.Map;

public final class RpgProfiles {

    private static final SkillSlotId NORMAL = new SkillSlotId(1);
    private static final SkillSlotId SPECIAL = new SkillSlotId(2);

    public static void registerAll() {
        registerBard();
    }

    private static void registerBard() {
        CatalystFamilyId family = new CatalystFamilyId("bard");

        Map<SkillSlotId, SkillId> skillsBySlot = Map.of(
                NORMAL, BardSkills.BARDO_NORMAL_INFUSED,
                SPECIAL, BardSkills.BARDO_SPECIAL_CASTED
        );

        CatalystDefinition def = new CatalystDefinition(
                family,
                FluteItem::isFlute,
                skillsBySlot
        );

        Map<SkillId, SkillTuning> tunings = Map.of(
                BardSkills.BARDO_NORMAL_INFUSED,
                new SkillTuning(
                        Duration.ofTicks(0),
                        Duration.ofTicks(100),
                        Duration.ofTicks(0),
                        Duration.ofTicks(10),
                        Duration.ofTicks(26)
                ),
                BardSkills.BARDO_SPECIAL_CASTED,
                new SkillTuning(
                        Duration.ofTicks(40),
                        Duration.ofTicks(200),
                        Duration.ofTicks(10),
                        Duration.ofTicks(10),
                        Duration.ofTicks(0)
                )
        );

        RpgRegistry.catalysts().register(def);

        CatalystProfile profile = new CatalystProfile(
                def,
                new BardFluteEffects(),
                tunings
        );

        RpgRegistry.profiles().register(profile);

        for (var e : tunings.entrySet()) {
            RpgRegistry.skillTunings().register(e.getKey(), e.getValue());
        }
    }

    private RpgProfiles() {}
}