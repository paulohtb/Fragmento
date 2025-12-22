package com.pgalaxyp.fragmento.cosmetics.network.s2c;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import java.util.Objects;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public record S2CSetBaseCosmeticResultPayload(int slotOrdinal, boolean success, String errorCode, long version) implements CustomPacketPayload {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Type<S2CSetBaseCosmeticResultPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "s2c_set_base_cosmetic_result"));

    public static final StreamCodec<ByteBuf, S2CSetBaseCosmeticResultPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            S2CSetBaseCosmeticResultPayload::slotOrdinal,
            ByteBufCodecs.BOOL,
            S2CSetBaseCosmeticResultPayload::success,
            ByteBufCodecs.STRING_UTF8,
            S2CSetBaseCosmeticResultPayload::errorCode,
            ByteBufCodecs.VAR_LONG,
            S2CSetBaseCosmeticResultPayload::version,
            S2CSetBaseCosmeticResultPayload::new
    );

    public S2CSetBaseCosmeticResultPayload {
        Objects.requireNonNull(errorCode, "errorCode");
    }

    public CosmeticSlot slot() {
        CosmeticSlot[] values = CosmeticSlot.values();
        int i = slotOrdinal;
        if (i < 0 || i >= values.length) {
            return null;
        }
        return values[i];
    }

    public static S2CSetBaseCosmeticResultPayload of(CosmeticSlot slot, boolean success, String errorCode, long version) {
        int ord = slot == null ? -1 : slot.ordinal();
        String code = errorCode == null ? "invalid" : errorCode;
        S2CSetBaseCosmeticResultPayload out = new S2CSetBaseCosmeticResultPayload(ord, success, code, version);
        LOGGER.debug("S2CSetBaseCosmeticResultPayload of slot {} ok {} code {} version {}", Integer.valueOf(ord), Boolean.valueOf(success), code, Long.valueOf(version));
        return out;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}