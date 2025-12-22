package com.pgalaxyp.fragmento.cosmetics.network.s2c;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import io.netty.buffer.ByteBuf;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public record S2CSyncCosmeticsPayload(UUID playerId, CosmeticLoadout effective, long version) implements CustomPacketPayload {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Type<S2CSyncCosmeticsPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "s2c_sync_cosmetics"));

    public static final StreamCodec<ByteBuf, S2CSyncCosmeticsPayload> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public S2CSyncCosmeticsPayload decode(ByteBuf buf) {
            UUID id = UUIDUtil.STREAM_CODEC.decode(buf);
            long version = ByteBufCodecs.VAR_LONG.decode(buf);
            int size = ByteBufCodecs.VAR_INT.decode(buf);

            EnumMap<CosmeticSlot, CosmeticId> map = new EnumMap<>(CosmeticSlot.class);
            for (int i = 0; i < size; i++) {
                int ord = ByteBufCodecs.VAR_INT.decode(buf);
                ResourceLocation rl = ResourceLocation.STREAM_CODEC.decode(buf);
                CosmeticSlot slot = ord >= 0 && ord < CosmeticSlot.values().length
                        ? CosmeticSlot.values()[ord]
                        : null;
                if (slot != null) {
                    map.put(slot, CosmeticId.of(rl));
                }
            }

            CosmeticLoadout loadout = new CosmeticLoadout(map);
            LOGGER.debug("S2CSyncCosmeticsPayload decode player {} slots {} version {}", id, loadout.equippedView().size(), version);
            return new S2CSyncCosmeticsPayload(id, loadout, version);
        }

        @Override
        public void encode(ByteBuf buf, S2CSyncCosmeticsPayload value) {
            UUIDUtil.STREAM_CODEC.encode(buf, value.playerId);
            ByteBufCodecs.VAR_LONG.encode(buf, value.version);

            Map<CosmeticSlot, CosmeticId> equipped = value.effective.equippedView();
            ByteBufCodecs.VAR_INT.encode(buf, equipped.size());
            for (Map.Entry<CosmeticSlot, CosmeticId> e : equipped.entrySet()) {
                ByteBufCodecs.VAR_INT.encode(buf, e.getKey().ordinal());
                ResourceLocation.STREAM_CODEC.encode(buf, e.getValue().value());
            }

            LOGGER.debug("S2CSyncCosmeticsPayload encode player {} slots {} version {}", value.playerId, equipped.size(), value.version);
        }
    };

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}