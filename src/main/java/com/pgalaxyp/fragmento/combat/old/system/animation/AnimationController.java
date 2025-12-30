package com.pgalaxyp.fragmento.combat.old.system.animation;

import com.pgalaxyp.fragmento.combat.old.system.entity.controller.EntityController;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

public class AnimationController<T extends Entity & GeoAnimatable> extends EntityController<T> {

    private final Supplier<RawAnimation> supplier;
    private RawAnimation last;

    public AnimationController(T entity, Supplier<RawAnimation> supplier) {
        super(entity);
        this.supplier = supplier;
    }

    protected Supplier<RawAnimation> getAnimationSupplier() {
        return supplier;
    }

    private PlayState predicate(AnimationState<T> state) {
        RawAnimation anim = getAnimationSupplier().get();

        if (anim == null) {
            if (last != null) {
                state.getController().stop();
                state.getController().forceAnimationReset();
                last = null;
            }
            return PlayState.STOP;
        }

        if (anim != last) {
            state.getController().setAnimation(anim);
            state.getController().forceAnimationReset();
            last = anim;
        }

        return PlayState.CONTINUE;
    }

    public void register(AnimatableManager.ControllerRegistrar registrar, String name) {
        registrar.add(
                new software.bernie.geckolib.animation.AnimationController<>(
                        entity,
                        name,
                        0,
                        this::predicate
                )
        );
    }

    @Override
    protected void onTick() {
    }
}