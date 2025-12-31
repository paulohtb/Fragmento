package com.pgalaxyp.fragmento.combat.content.entity.flute;

import com.pgalaxyp.fragmento.combat.content.catalyst.flute.FluteHitSpec;
import com.pgalaxyp.fragmento.combat.domain.hit.HitResult;
import com.pgalaxyp.fragmento.combat.engine.entity.CombatHitEntity;
import com.pgalaxyp.fragmento.combat.engine.targeting.TargetResolver;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class FluteBasicHitEntity extends CombatHitEntity {

    private final TargetResolver resolver;
    private FluteHitSpec spec;

    public FluteBasicHitEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.resolver = new TargetResolver();
    }

    public void configureSpec(FluteHitSpec spec) {
        this.spec = spec;
    }

    @Override
    protected HitResult performHit(ServerLevel level) {
        LivingEntity owner = resolveOwner(level);
        if (owner == null || spec == null) {
            return HitResult.miss();
        }

        Vec3 origin = owner.position().add(0.0, owner.getBbHeight() * 0.6, 0.0);

        LivingEntity target = resolver.findFirst(
                level,
                origin,
                spec.range(),
                e -> e.isAlive() && e != owner
        );

        if (target == null) {
            return HitResult.miss();
        }

        return damageTarget(level, owner, target);
    }
}