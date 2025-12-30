package com.pgalaxyp.fragmento.combat.old.system.skill;

import net.minecraft.world.item.ItemStack;

public interface Skill {

    double getRange(ItemStack stack);

    SkillResult execute(SkillContext ctx);
}
