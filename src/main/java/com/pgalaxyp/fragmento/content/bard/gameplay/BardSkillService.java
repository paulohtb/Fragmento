package com.pgalaxyp.fragmento.content.bard.gameplay;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardChargeData;
import com.pgalaxyp.fragmento.gameplay.skill.Skill;
import com.pgalaxyp.fragmento.gameplay.skill.SkillContext;
import com.pgalaxyp.fragmento.gameplay.skill.SkillMode;
import com.pgalaxyp.fragmento.gameplay.skill.SkillResult;
import com.pgalaxyp.fragmento.gameplay.skill.SkillSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class BardSkillService {

    private BardSkillService() {
    }

    public static SkillResult execute(
            ServerPlayer player,
            ItemStack stack,
            SkillSlot slot,
            LivingEntity target
    ) {
        if (player == null || stack == null || stack.isEmpty() || slot == null) {
            return SkillResult.failure();
        }

        if (!(stack.getItem() instanceof BardCatalystItem instrument)) {
            return SkillResult.failure();
        }

        if (!(player.level() instanceof ServerLevel level)) {
            return SkillResult.failure();
        }

        Skill skill = instrument.getSkill(slot);
        if (skill == null) {
            return SkillResult.failure();
        }

        LivingEntity finalTarget = target;
        if (slot == SkillSlot.SPECIAL && !(finalTarget instanceof Player)) {
            finalTarget = player;
        }

        SkillMode mode = BardCatalystStateService.resolveMode(stack, slot);

        SkillContext ctx = new SkillContext(
                level,
                player,
                stack,
                finalTarget,
                slot,
                mode
        );

        SkillResult result = skill.execute(ctx);
        if (!result.success()) {
            return result;
        }

        if (result.consumedCharge() && slot != SkillSlot.SPECIAL) {
            BardChargeData.reset(stack);
        }

        instrument.onSkillExecuted(stack, slot);

        return result;
    }
}
