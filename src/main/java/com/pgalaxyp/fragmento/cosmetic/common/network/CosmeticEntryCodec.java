package com.pgalaxyp.fragmento.cosmetic.common.network;

import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticEntry;
import com.pgalaxyp.fragmento.cosmetic.common.model.CosmeticId;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class CosmeticEntryCodec {

    public static final StreamCodec<ByteBuf, CosmeticId> ID_CODEC =
            ByteBufCodecs.STRING_UTF8.map(CosmeticId::of, CosmeticId::value);

    public static final StreamCodec<ByteBuf, CosmeticEntry> ENTRY_CODEC =
            StreamCodec.composite(
                    CosmeticInfoCodec.CODEC, CosmeticEntry::info,
                    ByteBufCodecs.BOOL, CosmeticEntry::equipped,
                    ByteBufCodecs.VAR_LONG, CosmeticEntry::version,
                    CosmeticEntry::new
            );

    private CosmeticEntryCodec() {}
}