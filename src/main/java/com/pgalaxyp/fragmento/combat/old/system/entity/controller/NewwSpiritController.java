package com.pgalaxyp.fragmento.combat.old.system.entity.controller;

import com.pgalaxyp.fragmento.combat.old.system.entity.behavior.SpiritBehavior;
import com.pgalaxyp.fragmento.combat.old.system.entity.behavior.SpiritContext;
import com.pgalaxyp.fragmento.combat.old.system.entity.event.SpiritEventSource;
import com.pgalaxyp.fragmento.combat.old.system.entity.host.NewwSpiritEntityBase;
import com.pgalaxyp.fragmento.combat.old.system.entity.movement.LookPlan;
import com.pgalaxyp.fragmento.combat.old.system.entity.movement.MovementPlan;
import com.pgalaxyp.fragmento.combat.old.system.entity.movement.OrientationSolver;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillMode;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public final class NewwSpiritController extends EntityController<NewwSpiritEntityBase> {

    private final Supplier<SpiritBehavior> behaviorSupplier;
    private final Supplier<SkillMode> modeSupplier;

    private final MovementPlan movement = new MovementPlan();
    private final LookPlan look = new LookPlan();

    private final SpiritImpactController impactController = new SpiritImpactController();

    public NewwSpiritController(
            NewwSpiritEntityBase entity,
            Supplier<SpiritBehavior> behaviorSupplier,
            Supplier<SkillMode> modeSupplier
    ) {
        super(entity);
        this.behaviorSupplier = behaviorSupplier;
        this.modeSupplier = modeSupplier;
    }

    @Override
    protected void onTick() {
        if (!(entity.level() instanceof ServerLevel level)) {
            return;
        }

        SpiritBehavior behavior = behaviorSupplier.get();
        if (behavior == null) {
            return;
        }

        LivingEntity owner = entity.getOwner();
        LivingEntity target = entity.getTarget();

        movement.clear();
        look.clear();

        SpiritContext ctx = new SpiritContext(
                modeSupplier.get(),
                entity,
                owner,
                target,
                entity.position(),
                entity.getLifetime(),
                entity.isCasted(),
                entity.position(),
                entity.getSourceInstrumentUuid()
        );

        if (entity instanceof SpiritEventSource ev) {
            if (ev.consumeCasted()) {
                behavior.onCasted(ctx);
            }
            if (ev.consumeCancelled()) {
                behavior.onCancelled(ctx);
            }
        }

        behavior.tick(ctx, movement, look);

        if (movement.kind == MovementPlan.Kind.VELOCITY) {
            Vec3 v = movement.desiredVelocity;
            entity.moveServer(v);
        }

        impactController.tick(level, entity, behavior, ctx);

        OrientationSolver.apply(entity, look);
    }
}