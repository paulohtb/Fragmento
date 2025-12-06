package com.pgalaxyp.fragmento.feature.bard_class.common.data;

import net.minecraft.network.syncher.EntityDataAccessor;
import com.pgalaxyp.fragmento.feature.bard_class.common.spirit.SpiritBase;

public final class SpiritAnimationData {

    private SpiritAnimationData() {}

    public static final EntityDataAccessor<Integer> ANIM = SpiritBase.ANIM;
    public static final EntityDataAccessor<Boolean> HIT = SpiritBase.HIT;
}
