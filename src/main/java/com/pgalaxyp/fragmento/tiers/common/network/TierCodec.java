package com.pgalaxyp.fragmento.tiers.common.network;

import com.pgalaxyp.fragmento.tiers.common.model.Tier;
import com.pgalaxyp.fragmento.tiers.common.model.TierLevel;
import com.pgalaxyp.fragmento.tiers.common.model.TierStatus;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class TierCodec {

    public static final StreamCodec<ByteBuf, Tier> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    t -> t.level().value(),
                    ByteBufCodecs.VAR_INT,
                    t -> t.status().ordinal(),
                    TierCodec::read
            );

    private TierCodec() {}

    private static Tier read(int level, int status) {
        TierLevel lvl = TierLevel.of(level);
        TierStatus st = lvl.value() > 0 ? TierStatus.ACTIVE : TierStatus.INACTIVE;
        return new Tier(lvl, st);
    }
}