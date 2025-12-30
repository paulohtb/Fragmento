package com.pgalaxyp.fragmento.combat.old.system.gameplay;

import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystIdService;
import com.pgalaxyp.fragmento.combat.old.content.bard.catalyst.BardCatalystItem;
import com.pgalaxyp.fragmento.combat.old.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.combat.old.content.bard.gameplay.BardCatalystStateService;
import com.pgalaxyp.fragmento.combat.old.system.charge.ChargeInstance;
import com.pgalaxyp.fragmento.combat.old.system.charge.ChargeSystem;
import com.pgalaxyp.fragmento.combat.old.system.skill.Skill;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillContext;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillResult;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillSlot;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public final class BardSkillService {

    private BardSkillService() {
    }

    public static SkillResult execute(
            ChargeSystem charges,
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

        UUID instrumentId = BardCatalystIdService.getOrCreate(stack);
        if (instrumentId == null) {
            return SkillResult.failure();
        }

        SkillMode mode = BardCatalystStateService.resolveMode(charges, stack, slot);

        SkillContext ctx = new SkillContext(
                level,
                player,
                stack,
                target,
                slot,
                mode
        );

        SkillResult result = skill.execute(ctx);
        if (!result.success()) {
            return result;
        }

        ChargeInstance c = charges.getOrCreate(instrumentId, BardInstrumentConstants.MAX_CHARGE);

        if (slot == SkillSlot.BASIC && mode == SkillMode.BASIC) {
            c.increment();
        }

        if (mode == SkillMode.CHARGED && slot == SkillSlot.BASIC) {
            c.reset();
        }

        return result;
    }
}