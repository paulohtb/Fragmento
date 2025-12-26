package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.skill.SkillMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class BardSpiritImpactService {

    private BardSpiritImpactService() {
    }

    public static void handle(
            NewwSpiritEntityBase spirit,
            SpiritContext ctx,
            LivingEntity hit
    ) {
        if (spirit == null || ctx == null || hit == null) {
            return;
        }

        if (!(spirit.level() instanceof ServerLevel)) {
            return;
        }

        float dmg = resolveDamage(ctx.mode);
        if (dmg <= 0.0F) {
            return;
        }

        LivingEntity owner = spirit.getOwner();

        Vec3 before = hit.getDeltaMovement();

        DamageSource source;
        if (owner != null) {
            source = hit.damageSources().indirectMagic(spirit, owner);
        } else {
            source = hit.damageSources().magic();
        }

        boolean applied = hit.hurt(source, dmg);

        if (applied) {
            Vec3 after = hit.getDeltaMovement();
            if (!after.equals(before)) {
                hit.setDeltaMovement(before);
                hit.hurtMarked = true;
            }
        }

        spirit.markCasted();
    }

    private static float resolveDamage(SkillMode mode) {
        if (mode == SkillMode.BASIC) {
            return BardInstrumentConstants.BASIC_DAMAGE;
        }

        if (mode == SkillMode.CHARGED) {
            return BardInstrumentConstants.CHARGED_DAMAGE;
        }

        if (mode == SkillMode.SPECIAL) {
            return 10.0F;
        }

        return 0.0F;
    }
}