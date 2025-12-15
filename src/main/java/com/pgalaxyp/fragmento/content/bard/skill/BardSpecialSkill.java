package com.pgalaxyp.fragmento.content.bard.skill;

import com.pgalaxyp.fragmento.content.bard.entity.BardSkillEntityBase;
import com.pgalaxyp.fragmento.gameplay.skill.Skill;
import com.pgalaxyp.fragmento.gameplay.skill.SkillContext;
import com.pgalaxyp.fragmento.gameplay.skill.SkillMode;
import com.pgalaxyp.fragmento.gameplay.skill.SkillResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public record BardSpecialSkill<S extends BardSkillEntityBase>(
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
