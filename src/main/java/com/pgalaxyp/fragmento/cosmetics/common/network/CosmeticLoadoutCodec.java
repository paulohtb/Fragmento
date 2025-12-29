package com.pgalaxyp.fragmento.cosmetics.common.network;

import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.common.model.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class CosmeticLoadoutCodec {

    public static final StreamCodec<ByteBuf, CosmeticId> ID_CODEC =
            ByteBufCodecs.STRING_UTF8.map(CosmeticId::of, CosmeticId::value);

    private static final StreamCodec<ByteBuf, Map<CosmeticSlot, CosmeticId>> MAP_CODEC =
            ByteBufCodecs.map(CosmeticLoadoutCodec::newEnumMap, CosmeticSlotCodec.STREAM_CODEC, ID_CODEC, 32);

    public static final StreamCodec<ByteBuf, CosmeticLoadout> STREAM_CODEC =
            MAP_CODEC.map(CosmeticLoadout::new, CosmeticLoadout::view);

    private CosmeticLoadoutCodec() {}

    private static EnumMap<CosmeticSlot, CosmeticId> newEnumMap(int ignored) {
        return new EnumMap<>(CosmeticSlot.class);
    }
}