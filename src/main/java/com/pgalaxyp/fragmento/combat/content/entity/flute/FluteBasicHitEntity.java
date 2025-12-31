package com.pgalaxyp.fragmento.combat.content.entity.flute;

import com.pgalaxyp.fragmento.combat.engine.entity.CombatHitEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public final class FluteBasicHitEntity extends CombatHitEntity {

    public FluteBasicHitEntity(EntityType<? extends FluteBasicHitEntity> type, Level level) {
        super(type, level);
    }

    public void configure(
            net.minecraft.server.level.ServerPlayer player,
            com.pgalaxyp.fragmento.combat.state.runtime.ServerCombatState state
    ) {
        this.ownerId = player.getUUID();
        this.damage = 4.0f;
        this.combatState = state;
    }

    @Override
    protected com.pgalaxyp.fragmento.combat.domain.hit.HitResult performHit(
            net.minecraft.server.level.ServerLevel level
    ) {
        return com.pgalaxyp.fragmento.combat.domain.hit.HitResult.miss();
    }
}