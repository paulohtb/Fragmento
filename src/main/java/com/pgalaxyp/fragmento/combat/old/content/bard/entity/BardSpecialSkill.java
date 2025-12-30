package com.pgalaxyp.fragmento.combat.old.content.bard.entity;

import com.pgalaxyp.fragmento.combat.old.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.combat.old.system.skill.Skill;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillContext;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public record BardSpecialSkill<S extends NewwSpiritEntityBase>(
        Function<ServerLevel, S> factory
) implements Skill {

    @Override
    public double getRange(ItemStack stack) {
        return 15.0;
    }

    @Override
    public SkillResult execute(SkillContext ctx) {
        S spirit = factory.apply(ctx.level());
        if (spirit == null) {
            return SkillResult.failure();
        }

        spirit.summon(
                ctx.caster(),
                ctx.target(),
                ctx.level(),
                SkillMode.SPECIAL,
                ctx.itemStack()
        );

        return SkillResult.successNoCooldown();
    }
}
