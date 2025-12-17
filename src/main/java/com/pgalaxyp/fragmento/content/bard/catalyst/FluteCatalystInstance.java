package com.pgalaxyp.fragmento.content.bard.catalyst;


import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.content.bard.entity.BardNormalSkill;
import com.pgalaxyp.fragmento.content.bard.entity.FluteBehaviorFactory;
import com.pgalaxyp.fragmento.content.bard.registry.FluteSkillEntityRegistry;
import com.pgalaxyp.fragmento.content.bard.entity.BardSpecialSkill;
import com.pgalaxyp.fragmento.system.entity.host.BardSpiritEntity;
import com.pgalaxyp.fragmento.system.skill.Skill;
import com.pgalaxyp.fragmento.system.skill.SkillMode;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.function.Function;

public final class FluteCatalystInstance extends BardCatalystItem {

    public FluteCatalystInstance(Properties props) {
        super(props, createSkills());
    }

    private static List<Skill> createSkills() {
        Function<ServerLevel, BardSpiritEntity> basic = level -> {
            BardSpiritEntity e = new BardSpiritEntity(
                    FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
                    level
            );
            e.configure(
                    FluteBehaviorFactory.create(SkillMode.BASIC),
                    SkillMode.BASIC
            );
            return e;
        };

        Function<ServerLevel, BardSpiritEntity> charged = level -> {
            BardSpiritEntity e = new BardSpiritEntity(
                    FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
                    level
            );
            e.configure(
                    FluteBehaviorFactory.create(SkillMode.CHARGED),
                    SkillMode.CHARGED
            );
            return e;
        };

        Function<ServerLevel, BardSpiritEntity> special = level -> {
            BardSpiritEntity e = new BardSpiritEntity(
                    FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
                    level
            );
            e.configure(
                    FluteBehaviorFactory.create(SkillMode.SPECIAL),
                    SkillMode.SPECIAL
            );
            return e;
        };

        return List.of(
                new BardNormalSkill(
                        basic,
                        charged,
                        BardInstrumentConstants.BASIC_COOLDOWN,
                        BardInstrumentConstants.CHARGED_COOLDOWN
                ),
                new BardSpecialSkill<>(special)
        );
    }

    @Override
    public void onSkillExecuted(ItemStack stack, SkillSlot slot) {
        if (slot == SkillSlot.SPECIAL) return;
    }
}