package com.pgalaxyp.fragmento.content.bard.skill;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.system.channel.ChannelingService;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.skill.Skill;
import com.pgalaxyp.fragmento.system.skill.SkillResult;
import com.pgalaxyp.fragmento.system.skill.SkillSlot;
import com.pgalaxyp.fragmento.system.skill.SkillTargetingService;
import com.pgalaxyp.fragmento.system.skill.SkillStateSnapshotDispatch;
import com.pgalaxyp.fragmento.system.skill.ServerSkillStateServices;
import com.pgalaxyp.fragmento.system.charge.ChargeSystem;
import com.pgalaxyp.fragmento.system.gameplay.BardSkillService;
import com.pgalaxyp.fragmento.system.gameplay.cooldown.PlayerSkillCooldownService;
import com.pgalaxyp.fragmento.content.bard.gameplay.BardCatalystVisualCooldownService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public final class BardSkillServerController {

    private static final int SPECIAL_CANCEL_COOLDOWN = 20;

    private BardSkillServerController() {
    }

    public static void handleStart(ServerPlayer player, SkillSlot slot, int targetId) {
        if (player == null || slot == null) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        var stack = player.getMainHandItem();
        if (stack.isEmpty()) return;
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
            net.minecraft.world.item.ItemStack stack,
            double range,
            int targetId
    ) {
        LivingEntity target =
                SkillTargetingService.resolveBasicLivingTarget(level, player, range, targetId);
        if (target == null) return;

        UUID instrumentId = BardCatalystIdService.getOrCreate(stack);
        if (instrumentId == null) return;

        ChargeSystem charges = ServerSkillStateServices.charges(player.getServer());

        SkillResult result =
                BardSkillService.execute(charges, player, stack, SkillSlot.BASIC, target);

        if (!result.success()) return;

        int cd = Math.max(0, result.cooldownTicks());
        if (cd > 0) {
            PlayerSkillCooldownService.apply(player, SkillSlot.BASIC, cd);
            BardCatalystVisualCooldownService.apply(player, cd);
        }

        SkillStateSnapshotDispatch.send(player, instrumentId);
    }

    private static void handleSpecialStart(
            ServerPlayer player,
            ServerLevel level,
            net.minecraft.world.item.ItemStack stack,
            double range,
            int targetId
    ) {
        if (ChannelingService.consumePendingCancel(player)) return;
        if (ChannelingService.hasActive(player)) return;

        ServerPlayer finalTarget =
                SkillTargetingService.resolveSpecialPlayerTargetOrSelf(level, player, range, targetId);

        UUID instrumentId = BardCatalystIdService.getOrCreate(stack);
        if (instrumentId == null) return;

        ChargeSystem charges = ServerSkillStateServices.charges(player.getServer());

        SkillResult result =
                BardSkillService.execute(charges, player, stack, SkillSlot.SPECIAL, finalTarget);

        if (!result.success()) return;

        NewwSpiritEntityBase spirit = findNewestOwnedSpirit(level, player, instrumentId);
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

        SkillStateSnapshotDispatch.send(player, instrumentId);
    }

    private static NewwSpiritEntityBase findNewestOwnedSpirit(ServerLevel level, ServerPlayer player, UUID instrumentId) {
        AABB area = com.pgalaxyp.fragmento.core.util.AabbUtil.expand(player.getBoundingBox(), 12.0);

        Predicate<NewwSpiritEntityBase> filter = e -> {
            if (e == null) return false;
            UUID id = e.getOwnerUuid();
            if (id == null || !id.equals(player.getUUID())) return false;
            UUID src = e.getSourceInstrumentUuid();
            return src != null && src.equals(instrumentId);
        };

        List<NewwSpiritEntityBase> list = level.getEntitiesOfClass(
                NewwSpiritEntityBase.class,
                area,
                filter
        );

        if (list.isEmpty()) return null;

        NewwSpiritEntityBase best = null;
        int bestLife = Integer.MAX_VALUE;

        for (NewwSpiritEntityBase e : list) {
            int life = e.getLifetime();
            if (life <= bestLife) {
                bestLife = life;
                best = e;
            }
        }

        return best;
    }
}