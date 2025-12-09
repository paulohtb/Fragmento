package com.pgalaxyp.fragmento.core.controller;

import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class BehaviorAnimationController<T extends Entity & GeoAnimatable> extends AnimationController<T> {

    private final Map<String, RawAnimation> animations = new HashMap<>();
    private final Function<T, String> keyGetter;

    public BehaviorAnimationController(T entity, Function<T, String> keyGetter) {
        super(entity, () -> null);
        this.keyGetter = keyGetter;
    }

    public void registerState(String id, RawAnimation anim) {
        animations.put(id, anim);
    }

    @Override
    protected Supplier<RawAnimation> getAnimationSupplier() {
        return () -> {
            String key = keyGetter.apply(entity);
            if (key == null || key.isEmpty()) return null;
            return animations.get(key);
        };
    }
}
