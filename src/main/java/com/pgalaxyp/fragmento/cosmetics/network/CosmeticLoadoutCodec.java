package com.pgalaxyp.fragmento.cosmetics.network;

import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class CosmeticLoadoutCodec {

    public static final StreamCodec<ByteBuf, CosmeticId> ID_CODEC =
            ByteBufCodecs.STRING_UTF8.map(
                    CosmeticId::of,
                    CosmeticId::value
            );

    private static final StreamCodec<ByteBuf, Map<CosmeticSlot, CosmeticId>> MAP_CODEC =
            ByteBufCodecs.map(
                    CosmeticLoadoutCodec::newEnumMap,
                    CosmeticSlotCodec.STREAM_CODEC,
                    ID_CODEC,
                    32
            );

    public static final StreamCodec<ByteBuf, CosmeticLoadout> STREAM_CODEC =
            MAP_CODEC.map(
                    CosmeticLoadout::new,
                    CosmeticLoadout::view
            );

    private CosmeticLoadoutCodec() {
    }

    private static EnumMap<CosmeticSlot, CosmeticId> newEnumMap(int ignored) {
        return new EnumMap<>(CosmeticSlot.class);
    }
}