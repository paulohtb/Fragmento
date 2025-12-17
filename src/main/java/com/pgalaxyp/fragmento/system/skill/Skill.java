package com.pgalaxyp.fragmento.system.skill;

import net.minecraft.world.item.ItemStack;

public interface Skill {

    double getRange(ItemStack stack);

    SkillResult execute(SkillContext ctx);
}
