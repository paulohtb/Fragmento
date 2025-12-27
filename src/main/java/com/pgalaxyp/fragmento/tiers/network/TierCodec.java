package com.pgalaxyp.fragmento.tiers.network;

import com.pgalaxyp.fragmento.tiers.api.Tier;
import com.pgalaxyp.fragmento.tiers.api.TierLevel;
import com.pgalaxyp.fragmento.tiers.api.TierStatus;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class TierCodec {

    public static final StreamCodec<ByteBuf, Tier> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    tier -> tier.level().value(),
                    ByteBufCodecs.VAR_INT,
                    tier -> tier.status().ordinal(),
                    TierCodec::read
            );

    private TierCodec() {}

    private static Tier read(int lvl, int st) {
        TierLevel level = TierLevel.of(lvl);
        TierStatus[] values = TierStatus.values();
        TierStatus status = st >= 0 && st < values.length ? values[st] : TierStatus.UNKNOWN;
        return new Tier(level, status);
    }
}