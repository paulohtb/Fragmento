package com.pgalaxyp.fragmento.content.bard.catalyst;

import com.pgalaxyp.fragmento.content.bard.entity.FluteSkillEntity;
import com.pgalaxyp.fragmento.content.bard.skill.BardNormalSkill;
import com.pgalaxyp.fragmento.content.bard.skill.BardSpecialSkill;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.content.bard.registry.FluteSkillEntityRegistry;
import com.pgalaxyp.fragmento.system.skill.Skill;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class FluteCatalystInstance extends BardCatalystItem {

    public FluteCatalystInstance(Properties props) {
        super(props, createSkills());
    }

    private static List<Skill> createSkills() {
        return List.of(
                new BardNormalSkill(
                        level -> new FluteSkillEntity(
                                FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
                                level
                        ),
                        level -> new FluteSkillEntity(
                                FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
                                level
                        ),
                        BardInstrumentConstants.BASIC_COOLDOWN,
                        BardInstrumentConstants.CHARGED_COOLDOWN
                ),
                new BardSpecialSkill<>(
                        level -> new FluteSkillEntity(
                                FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
                                level
                        )
                )
        );
    }

    @Override
    public void onSkillExecuted(ItemStack stack, SkillSlot slot) {
        if (slot == SkillSlot.SPECIAL) return;
    }
}
