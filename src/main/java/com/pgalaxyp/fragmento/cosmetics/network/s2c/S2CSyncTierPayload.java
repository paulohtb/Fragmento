package com.pgalaxyp.fragmento.cosmetics.network.s2c;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public record S2CSyncTierPayload(int tierLevel, long version) implements CustomPacketPayload {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Type<S2CSyncTierPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "s2c_sync_tier"));

    public static final StreamCodec<ByteBuf, S2CSyncTierPayload> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT,
        S2CSyncTierPayload::tierLevel,
        ByteBufCodecs.VAR_LONG,
        S2CSyncTierPayload::version,
        S2CSyncTierPayload::new
    );

    public CosmeticTier tier() {
        return CosmeticTier.fromLevel(tierLevel);
    }

    public static S2CSyncTierPayload of(CosmeticTier tier, long version) {
        CosmeticTier t = tier == null ? CosmeticTier.TIER_0 : tier;
        S2CSyncTierPayload out = new S2CSyncTierPayload(t.level(), version);
        LOGGER.debug("S2CSyncTierPayload of, tier {}, version {}", t.name(), Long.valueOf(version));
        return out;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}