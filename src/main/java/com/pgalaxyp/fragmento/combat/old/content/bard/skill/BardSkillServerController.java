package com.pgalaxyp.fragmento.combat.old.content.bard.skill;

import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.combat.old.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.combat.old.content.bard.gameplay.BardCatalystVisualCooldownService;
import com.pgalaxyp.fragmento.combat.old.system.channel.ChannelingService;
import com.pgalaxyp.fragmento.combat.old.system.charge.ChargeSystem;
import com.pgalaxyp.fragmento.combat.old.system.gameplay.BardSkillService;
import com.pgalaxyp.fragmento.combat.old.system.skill.ServerSkillStateServices;
import com.pgalaxyp.fragmento.combat.old.system.skill.Skill;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillResult;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillSlot;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillStateSnapshotDispatch;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillTargetingService;
import com.pgalaxyp.fragmento.combat.old.system.gameplay.cooldown.PlayerSkillCooldownService;
import com.pgalaxyp.fragmento.combat.old.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.foundation.AabbUtil;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public final class BardSkillServerController {

    private static final int SPECIAL_CANCEL_COOLDOWN = 20;

    private BardSkillServerController() {
    }

    public static void handleStart(ServerPlayer player, SkillSlot slot, int targetId) {
        if (player == null || slot == null) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        HeldInstrument held = resolveHeldInstrument(player);
        if (held == null) return;

        if (!PlayerSkillCooldownService.canUse(player, slot)) return;

        Skill skill = held.instrument().getSkill(slot);
        if (skill == null) return;

        double range = skill.getRange(held.stack());

        if (slot == SkillSlot.SPECIAL) {
            handleSpecialStart(player, level, held, range, targetId);
            return;
        }

        if (slot == SkillSlot.BASIC) {
            handleBasicStart(player, level, held, range, targetId);
        }
    }

    private static HeldInstrument resolveHeldInstrument(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty()) return null;
        if (!(stack.getItem() instanceof BardCatalystItem instrument)) return null;

        UUID instrumentId = BardCatalystIdService.getOrCreate(stack);
        if (instrumentId == null) return null;

        return new HeldInstrument(stack, instrument, instrumentId);
    }

    private static void handleBasicStart(
            ServerPlayer player,
            ServerLevel level,
            HeldInstrument held,
            double range,
            int targetId
    ) {
        LivingEntity target =
                SkillTargetingService.resolveBasicLivingTarget(level, player, range, targetId);
        if (target == null) return;

        ChargeSystem charges = ServerSkillStateServices.charges(player.getServer());

        SkillResult result =
                BardSkillService.execute(charges, player, held.stack(), SkillSlot.BASIC, target);

        if (!result.success()) return;

        int cd = Math.max(0, result.cooldownTicks());
        if (cd > 0) {
            PlayerSkillCooldownService.apply(player, SkillSlot.BASIC, cd);
            BardCatalystVisualCooldownService.apply(player, cd);
        }

        SkillStateSnapshotDispatch.send(player, held.instrumentId());
    }

    private static void handleSpecialStart(
            ServerPlayer player,
            ServerLevel level,
            HeldInstrument held,
            double range,
            int targetId
    ) {
        if (ChannelingService.consumePendingCancel(player)) return;
        if (ChannelingService.hasActive(player)) return;

        ServerPlayer finalTarget =
                SkillTargetingService.resolveSpecialPlayerTargetOrSelf(level, player, range, targetId);

        ChargeSystem charges = ServerSkillStateServices.charges(player.getServer());

        SkillResult result =
                BardSkillService.execute(charges, player, held.stack(), SkillSlot.SPECIAL, finalTarget);

        if (!result.success()) return;

        NewwSpiritEntityBase spirit = findNewestOwnedSpirit(level, player, held.instrumentId());
        if (spirit == null) return;

        ChannelingService.start(
                player,
                SkillSlot.SPECIAL,
                finalTarget.getId(),
                spirit.getId(),
                InteractionHand.MAIN_HAND,
                held.stack(),
                BardInstrumentConstants.SPECIAL_COOLDOWN,
                SPECIAL_CANCEL_COOLDOWN
        );

        SkillStateSnapshotDispatch.send(player, held.instrumentId());
    }

    private static NewwSpiritEntityBase findNewestOwnedSpirit(ServerLevel level, ServerPlayer player, UUID instrumentId) {
        AABB area = AabbUtil.expand(player.getBoundingBox(), 12.0);

        Predicate<NewwSpiritEntityBase> filter = e -> {
            if (e == null) return false;

            UUID owner = e.getOwnerUuid();
            if (owner == null || !owner.equals(player.getUUID())) return false;

            UUID src = e.getSourceInstrumentUuid();
            return src != null && src.equals(instrumentId);
        };

        List<NewwSpiritEntityBase> list = level.getEntitiesOfClass(NewwSpiritEntityBase.class, area, filter);
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

    private record HeldInstrument(ItemStack stack, BardCatalystItem instrument, UUID instrumentId) {
    }
}