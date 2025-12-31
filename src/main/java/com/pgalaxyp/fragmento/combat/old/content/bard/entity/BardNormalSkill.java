//package com.pgalaxyp.fragmento.combat.old.content.bard.entity;
//
//import com.pgalaxyp.fragmento.combat.old.system.entity.host.NewwSpiritEntityBase;
//import com.pgalaxyp.fragmento.combat.old.system.skill.Skill;
//import com.pgalaxyp.fragmento.combat.old.system.skill.SkillContext;
//import com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode;
//import com.pgalaxyp.fragmento.combat.old.system.skill.SkillResult;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.item.ItemStack;
//
//import java.util.function.Function;
//
//public record BardNormalSkill(
//        Function<ServerLevel, ? extends NewwSpiritEntityBase> basicFactory,
//        Function<ServerLevel, ? extends NewwSpiritEntityBase> chargedFactory,
//        int basicCooldown,
//        int chargedCooldown
//) implements Skill {
//
//    @Override
//    public double getRange(ItemStack stack) {
//        return 12.0;
//    }
//
//    @Override
//    public SkillResult execute(SkillContext ctx) {
//        boolean charged = ctx.mode() == SkillMode.CHARGED;
//
//        NewwSpiritEntityBase spirit = charged
//                ? chargedFactory.apply(ctx.level())
//                : basicFactory.apply(ctx.level());
//
//        if (spirit == null) {
//            return SkillResult.failure();
//        }
//
//        int id = spirit.summon(
//                ctx.caster(),
//                ctx.target(),
//                ctx.level(),
//                ctx.mode(),
//                ctx.itemStack()
//        );
//
//        if (id <= 0) {
//            return SkillResult.failure();
//        }
//
//        if (charged) {
//            return SkillResult.successWithCharge(chargedCooldown);
//        }
//
//        return SkillResult.successWithCooldown(basicCooldown);
//    }
//}