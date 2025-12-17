package com.pgalaxyp.fragmento.system.animation;

import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.RawAnimation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public final class BehaviorAnimationController<T extends Entity & GeoAnimatable>
        extends AnimationController<T> {

    private final Map<Byte, RawAnimation> animations = new HashMap<>();
    private final Supplier<RawAnimation> supplier;

    public BehaviorAnimationController(
            T entity,
            Function<T, Byte> keyGetter
    ) {
        super(entity, () -> null);
        this.supplier = () -> animations.get(keyGetter.apply(entity));
    }

    public void registerState(byte key, RawAnimation animation) {
        animations.put(key, animation);
    }

    @Override
    protected Supplier<RawAnimation> getAnimationSupplier() {
        return supplier;
    }
}
