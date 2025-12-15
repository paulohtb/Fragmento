package com.pgalaxyp.fragmento.content.bard.skill;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.content.bard.entity.BardSkillEntityBase;
import com.pgalaxyp.fragmento.content.bard.gameplay.BardCatalystVisualCooldownService;
import com.pgalaxyp.fragmento.gameplay.channel.ChannelingService;
import com.pgalaxyp.fragmento.gameplay.cooldown.PlayerSkillCooldownService;
import com.pgalaxyp.fragmento.gameplay.skill.SkillResult;
import com.pgalaxyp.fragmento.gameplay.skill.SkillSlot;
import com.pgalaxyp.fragmento.gameplay.skill.SkillTargetingService;
import com.pgalaxyp.fragmento.gameplay.skill.Skill;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.AABB;

public final class BardSkillServerController {

    private static final int SPECIAL_CANCEL_COOLDOWN = 10;

    private BardSkillServerController() {
    }

    public static void handleStart(ServerPlayer player, SkillSlot slot, int targetId) {
        if (!(player.level() instanceof ServerLevel level)) return;

        ItemStack stack = player.getMainHandItem();
        if (!(stack.getItem() instanceof BardCatalystItem instrument)) return;

        if (!PlayerSkillCooldownService.canUse(player, slot)) return;

        Skill skill = instrument.getSkill(slot);
        if (skill == null) return;

        double range = skill.getRange(stack);

        if (slot == SkillSlot.SPECIAL) {
            handleSpecialStart(player, level, stack, range, targetId);
            return;
        }

        if (slot == SkillSlot.BASIC) {
            handleBasicStart(player, level, stack, range, targetId);
        }
    }

    private static void handleBasicStart(
            ServerPlayer player,
            ServerLevel level,
            ItemStack stack,
            double range,
            int targetId
    ) {
        LivingEntity target =
                SkillTargetingService.resolveBasicLivingTarget(level, player, range, targetId);
        if (target == null) return;

        SkillResult result =
                com.pgalaxyp.fragmento.content.bard.gameplay.BardSkillService.execute(player, stack, SkillSlot.BASIC, target);

        if (!result.success()) return;

        int cd = Math.max(0, result.cooldownTicks());
        if (cd > 0) {
            PlayerSkillCooldownService.apply(player, SkillSlot.BASIC, cd);
            BardCatalystVisualCooldownService.apply(player, cd);
        }
    }

    private static void handleSpecialStart(
            ServerPlayer player,
            ServerLevel level,
            ItemStack stack,
            double range,
            int targetId
    ) {
        if (ChannelingService.consumePendingCancel(player)) return;
        if (ChannelingService.hasActive(player)) return;

        ServerPlayer finalTarget =
                SkillTargetingService.resolveSpecialPlayerTargetOrSelf(level, player, range, targetId);

        SkillResult result =
                com.pgalaxyp.fragmento.content.bard.gameplay.BardSkillService.execute(player, stack, SkillSlot.SPECIAL, finalTarget);

        if (!result.success()) return;

        BardSkillEntityBase spirit = findLatestOwnedSpirit(level, player);
        if (spirit == null) return;

        ChannelingService.start(
                player,
                SkillSlot.SPECIAL,
                finalTarget.getId(),
                spirit.getId(),
                InteractionHand.MAIN_HAND,
                stack,
                BardInstrumentConstants.SPECIAL_COOLDOWN,
                SPECIAL_CANCEL_COOLDOWN
        );
    }

    private static BardSkillEntityBase findLatestOwnedSpirit(ServerLevel level, ServerPlayer player) {
        AABB area = player.getBoundingBox().inflate(8);
        var list = level.getEntitiesOfClass(
                BardSkillEntityBase.class,
                area,
                e -> e.getOwnerUuid() != null && e.getOwnerUuid().equals(player.getUUID())
        );

        if (list.isEmpty()) return null;

        BardSkillEntityBase best = null;
        int bestLife = Integer.MIN_VALUE;

        for (int i = 0; i < list.size(); i++) {
            BardSkillEntityBase e = list.get(i);
            int life = e.getLifetime();
            if (life >= bestLife) {
                bestLife = life;
                best = e;
            }
        }

        return best;
    }
}
