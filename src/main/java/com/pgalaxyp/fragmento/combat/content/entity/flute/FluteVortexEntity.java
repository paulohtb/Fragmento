package com.pgalaxyp.fragmento.combat.content.entity.flute;

import com.pgalaxyp.fragmento.combat.domain.hit.HitResult;
import com.pgalaxyp.fragmento.combat.engine.entity.CombatHitEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class FluteVortexEntity extends CombatHitEntity {

    public FluteVortexEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    protected HitResult performHit(ServerLevel level) {
        LivingEntity owner = resolveOwner(level);
        if (owner == null) {
            return HitResult.miss();
        }

        Vec3 center = position();

        double radius = 4.5;
        AABB box = new AABB(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius
        );

        List<LivingEntity> targets =
                level.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && e != owner);

        if (targets.isEmpty()) {
            return HitResult.miss();
        }

        boolean damageApplied = false;

        for (LivingEntity target : targets) {
            Vec3 delta = center.subtract(target.position());
            double dist = Math.max(0.01, delta.length());
            Vec3 dir = delta.scale(1.0 / dist);

            double pull = 0.25;
            target.setDeltaMovement(
                    target.getDeltaMovement().add(
                            dir.x * pull,
                            0.25,
                            dir.z * pull
                    )
            );
            target.hurtMarked = true;
        }

        return HitResult.hitNoDamage();
    }
}