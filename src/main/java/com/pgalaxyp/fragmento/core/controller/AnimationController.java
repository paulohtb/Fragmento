package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.function.Supplier;

public class AnimationController<T extends Entity & GeoAnimatable> extends EntityController<T> {

    private final Supplier<RawAnimation> animationSupplier;
    private RawAnimation last;

    public AnimationController(T entity, Supplier<RawAnimation> supplier) {
        super(entity);
        this.animationSupplier = supplier;
    }

    protected Supplier<RawAnimation> getAnimationSupplier() {
        return animationSupplier;
    }

    private PlayState predicate(AnimationState<T> state) {
        RawAnimation anim = getAnimationSupplier().get();

        if (anim != null) {
            if (anim != last) {
                state.getController().setAnimation(anim);
                state.getController().forceAnimationReset();
                last = anim;
            }
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
    public void tick() {
    }

    @Override
    protected void onTick() {

    }
}
