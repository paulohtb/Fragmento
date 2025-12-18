package com.pgalaxyp.fragmento.system.skill;

import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.system.charge.ChargeSystem;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.gameplay.BardCatalystVisualCooldownService;
import com.pgalaxyp.fragmento.system.gameplay.BardSkillService;
import com.pgalaxyp.fragmento.system.gameplay.cooldown.PlayerSkillCooldownService;
import com.pgalaxyp.fragmento.system.channel.ChannelingService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public final class BardSkillServerController {

    private static final int SPECIAL_CANCEL_COOLDOWN = 10;

    private BardSkillServerController() {
    }

    public static void handleStart(ServerPlayer player, SkillSlot slot, int targetId) {
        if (player == null || slot == null) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        ItemStack stack = player.getMainHandItem();
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
            ItemStack stack,
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
            ItemStack stack,
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

        NewwSpiritEntityBase spirit = findLatestOwnedSpirit(level, player);
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

    private static NewwSpiritEntityBase findLatestOwnedSpirit(ServerLevel level, ServerPlayer player) {
        AABB area = expand(player.getBoundingBox(), 8.0);

        Predicate<NewwSpiritEntityBase> filter = new Predicate<>() {
            @Override
            public boolean test(NewwSpiritEntityBase e) {
                if (e == null) return false;
                UUID id = e.getOwnerUuid();
                return id != null && id.equals(player.getUUID());
            }
        };

        List<NewwSpiritEntityBase> list = level.getEntitiesOfClass(
                NewwSpiritEntityBase.class,
                area,
                filter
        );

        if (list.isEmpty()) return null;

        NewwSpiritEntityBase best = null;
        int bestLife = Integer.MIN_VALUE;

        for (int i = 0; i < list.size(); i++) {
            NewwSpiritEntityBase e = list.get(i);
            int life = e.getLifetime();
            if (life >= bestLife) {
                bestLife = life;
                best = e;
            }
        }

        return best;
    }

    private static AABB expand(AABB box, double amount) {
        if (box == null) return null;
        if (amount <= 0.0) return box;
        double a = amount;
        return new AABB(
                box.minX - a, box.minY - a, box.minZ - a,
                box.maxX + a, box.maxY + a, box.maxZ + a
        );
    }
}