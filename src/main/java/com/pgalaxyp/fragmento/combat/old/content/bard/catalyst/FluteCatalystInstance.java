//package com.pgalaxyp.fragmento.combat.old.content.bard.catalyst;
//
//
//import com.pgalaxyp.fragmento.combat.old.content.bard.constants.BardInstrumentConstants;
//import com.pgalaxyp.fragmento.combat.old.content.bard.entity.BardNormalSkill;
//import com.pgalaxyp.fragmento.combat.old.content.bard.entity.FluteBehaviorFactory;
//import com.pgalaxyp.fragmento.combat.old.content.bard.registry.FluteSkillEntityRegistry;
//import com.pgalaxyp.fragmento.combat.old.content.bard.entity.BardSpecialSkill;
//import com.pgalaxyp.fragmento.combat.old.system.entity.host.BardSpiritEntity;
//import com.pgalaxyp.fragmento.combat.old.system.skill.Skill;
//import com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode;
//import net.minecraft.server.level.ServerLevel;
//
//import java.util.List;
//import java.util.function.Function;
//
//public final class FluteCatalystInstance extends BardCatalystItem {
//
//    public FluteCatalystInstance(Properties props) {
//        super(props, createSkills());
//    }
//
//    private static List<Skill> createSkills() {
//        Function<ServerLevel, BardSpiritEntity> basic = level -> {
//            BardSpiritEntity e = new BardSpiritEntity(
//                    FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
//                    level
//            );
//            e.configure(
//                    FluteBehaviorFactory.create(SkillMode.BASIC),
//                    SkillMode.BASIC
//            );
//            return e;
//        };
//
//        Function<ServerLevel, BardSpiritEntity> charged = level -> {
//            BardSpiritEntity e = new BardSpiritEntity(
//                    FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
//                    level
//            );
//            e.configure(
//                    FluteBehaviorFactory.create(SkillMode.CHARGED),
//                    SkillMode.CHARGED
//            );
//            return e;
//        };
//
//        Function<ServerLevel, BardSpiritEntity> special = level -> {
//            BardSpiritEntity e = new BardSpiritEntity(
//                    FluteSkillEntityRegistry.FLUTE_SPIRIT.get(),
//                    level
//            );
//            e.configure(
//                    FluteBehaviorFactory.create(SkillMode.SPECIAL),
//                    SkillMode.SPECIAL
//            );
//            return e;
//        };
//
//        return List.of(
//                new BardNormalSkill(
//                        basic,
//                        charged,
//                        BardInstrumentConstants.BASIC_COOLDOWN,
//                        BardInstrumentConstants.CHARGED_COOLDOWN
//                ),
//                new BardSpecialSkill<>(special)
//        );
//    }
//}