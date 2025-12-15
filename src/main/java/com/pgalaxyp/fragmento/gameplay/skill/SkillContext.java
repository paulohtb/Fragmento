package com.pgalaxyp.fragmento.gameplay.skill;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public record SkillContext(
        ServerLevel level,
        ServerPlayer caster,
        ItemStack itemStack,
        LivingEntity target,
        SkillSlot slot,
        SkillMode mode
) {
}
