package com.pgalaxyp.fragmento.content.bard.entity;

import com.pgalaxyp.fragmento.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.system.skill.SkillMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public final class BardSpiritImpactService {

    private BardSpiritImpactService() {
    }

    public static void handle(
            NewwSpiritEntityBase spirit,
            SpiritContext ctx,
            LivingEntity hit
    ) {
        if (!(spirit.level() instanceof ServerLevel)) {
            return;
        }

        float dmg = resolveDamage(ctx.mode);
        if (dmg <= 0.0F) {
            return;
        }

        LivingEntity owner = spirit.getOwner();

        if (owner != null) {
            hit.hurt(owner.damageSources().magic(), dmg);
        } else {
            hit.hurt(hit.damageSources().magic(), dmg);
        }

        spirit.markCasted();
    }

    private static float resolveDamage(SkillMode mode) {
        if (mode == SkillMode.BASIC) {
            return 2.0F;
        }

        if (mode == SkillMode.CHARGED) {
            return 3.0F;
        }

        if (mode == SkillMode.SPECIAL) {
            return 10.0F;
        }

        return 0.0F;
    }
}