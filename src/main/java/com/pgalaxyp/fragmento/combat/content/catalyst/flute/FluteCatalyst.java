package com.pgalaxyp.fragmento.combat.content.catalyst.flute;

import com.pgalaxyp.fragmento.combat.content.entity.flute.FluteBasicHitEntity;
import com.pgalaxyp.fragmento.combat.content.entity.flute.FluteVortexEntity;
import com.pgalaxyp.fragmento.combat.domain.combo.ComboDefinition;
import com.pgalaxyp.fragmento.combat.rule.combo.ComboApplier;
import com.pgalaxyp.fragmento.combat.rule.skill.InfusedSkillRule;
import com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

public final class FluteCatalyst {

    private final ComboDefinition combo;
    private final InfusedSkillRule infusedRule;

    public FluteCatalyst(InfusedSkillRule infusedRule) {
        this.combo = FluteCombo.create();
        this.infusedRule = infusedRule;
    }

    public void performBasicAttack(
            ServerLevel level,
            Player player,
            ServerCombatState state,
            ComboApplier applier,
            EntityType<FluteBasicHitEntity> basicType,
            EntityType<FluteVortexEntity> vortexType
    ) {
        if (infusedRule.consumeIfArmed(state)) {
            FluteVortexEntity entity = new FluteVortexEntity(vortexType, level);
            entity.configure(
                    player.getUUID(),
                    0.0f,
                    state,
                    applier,
                    combo
            );
            entity.setPos(player.getX(), player.getY() + 1.0, player.getZ());
            level.addFreshEntity(entity);
            return;
        }

        int comboIndex = state.weapon().comboIndex();
        FluteHitSpec spec = FluteHitSpec.forComboIndex(comboIndex);

        FluteBasicHitEntity entity = new FluteBasicHitEntity(basicType, level);
        entity.configure(
                player.getUUID(),
                spec.damage(),
                state,
                applier,
                combo
        );
        entity.configureSpec(spec);
        entity.setPos(player.getX(), player.getY() + 1.0, player.getZ());
        level.addFreshEntity(entity);
    }
}