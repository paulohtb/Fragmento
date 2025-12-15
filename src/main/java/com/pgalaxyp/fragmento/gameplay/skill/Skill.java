package com.pgalaxyp.fragmento.gameplay.skill;

import net.minecraft.world.item.ItemStack;

public interface Skill {

    double getRange(ItemStack stack);

    SkillResult execute(SkillContext ctx);
}
