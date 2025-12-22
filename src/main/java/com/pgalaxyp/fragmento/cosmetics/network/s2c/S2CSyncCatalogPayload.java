package com.pgalaxyp.fragmento.cosmetics.network.s2c;

import com.pgalaxyp.fragmento.cosmetics.CosmeticsKeys;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticCatalogEntry;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record S2CSyncCatalogPayload(
        int playerTierLevel,
        int dataVersion,
        List<CosmeticCatalogEntry> unlocked,
        int tiers,
        int slots,
        int[] lockedCountsByTierSlot
) implements CustomPacketPayload {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final Type<S2CSyncCatalogPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(CosmeticsKeys.MOD_ID, "s2c_sync_catalog"));

    public static final StreamCodec<ByteBuf, S2CSyncCatalogPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public S2CSyncCatalogPayload decode(ByteBuf buf) {
            int tierLevel = ByteBufCodecs.VAR_INT.decode(buf);
            int dataVersion = ByteBufCodecs.VAR_INT.decode(buf);
            int entryCount = ByteBufCodecs.VAR_INT.decode(buf);

            ArrayList<CosmeticCatalogEntry> list = new ArrayList<>(entryCount);
            for (int i = 0; i < entryCount; i++) {
                ResourceLocation id = ResourceLocation.STREAM_CODEC.decode(buf);
                ResourceLocation type = ResourceLocation.STREAM_CODEC.decode(buf);
                int slotOrdinal = ByteBufCodecs.VAR_INT.decode(buf);
                int requiredTier = ByteBufCodecs.VAR_INT.decode(buf);
                int priority = ByteBufCodecs.VAR_INT.decode(buf);
                boolean visibleToSelf = ByteBufCodecs.BOOL.decode(buf);

                list.add(new CosmeticCatalogEntry(id, type, slotOrdinal, requiredTier, priority, visibleToSelf));
            }

            int tiers = ByteBufCodecs.VAR_INT.decode(buf);
            int slots = ByteBufCodecs.VAR_INT.decode(buf);
            int len = ByteBufCodecs.VAR_INT.decode(buf);

            int[] counts = new int[len];
            for (int i = 0; i < len; i++) {
                counts[i] = ByteBufCodecs.VAR_INT.decode(buf);
            }

            LOGGER.debug("S2CSyncCatalogPayload decode tier {} version {} unlocked {} counts {}", Integer.valueOf(tierLevel), Integer.valueOf(dataVersion), Integer.valueOf(list.size()), Integer.valueOf(len));
            return new S2CSyncCatalogPayload(tierLevel, dataVersion, list, tiers, slots, counts);
        }

        @Override
        public void encode(ByteBuf buf, S2CSyncCatalogPayload value) {
            ByteBufCodecs.VAR_INT.encode(buf, value.playerTierLevel);
            ByteBufCodecs.VAR_INT.encode(buf, value.dataVersion);

            List<CosmeticCatalogEntry> list = value.unlocked;
            ByteBufCodecs.VAR_INT.encode(buf, list.size());
            for (int i = 0; i < list.size(); i++) {
                CosmeticCatalogEntry e = list.get(i);
                ResourceLocation.STREAM_CODEC.encode(buf, e.id());
                ResourceLocation.STREAM_CODEC.encode(buf, e.type());
                ByteBufCodecs.VAR_INT.encode(buf, e.slotOrdinal());
                ByteBufCodecs.VAR_INT.encode(buf, e.requiredTierLevel());
                ByteBufCodecs.VAR_INT.encode(buf, e.priority());
                ByteBufCodecs.BOOL.encode(buf, e.visibleToSelf());
            }

            ByteBufCodecs.VAR_INT.encode(buf, value.tiers);
            ByteBufCodecs.VAR_INT.encode(buf, value.slots);

            int[] counts = value.lockedCountsByTierSlot;
            ByteBufCodecs.VAR_INT.encode(buf, counts.length);
            for (int i = 0; i < counts.length; i++) {
                ByteBufCodecs.VAR_INT.encode(buf, counts[i]);
            }

            LOGGER.debug("S2CSyncCatalogPayload encode tier {} version {} unlocked {} counts {}", Integer.valueOf(value.playerTierLevel), Integer.valueOf(value.dataVersion), Integer.valueOf(list.size()), Integer.valueOf(counts.length));
        }
    };

    public S2CSyncCatalogPayload {
        Objects.requireNonNull(unlocked, "unlocked");
        Objects.requireNonNull(lockedCountsByTierSlot, "lockedCountsByTierSlot");
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public int lockedCount(int tierLevel, int slotOrdinal) {
        if (tierLevel < 0) return 0;
        if (slotOrdinal < 0) return 0;
        if (tierLevel >= tiers) return 0;
        if (slotOrdinal >= slots) return 0;
        int idx = Math.addExact(Math.multiplyExact(tierLevel, slots), slotOrdinal);
        if (idx < 0) return 0;
        if (idx >= lockedCountsByTierSlot.length) return 0;
        return lockedCountsByTierSlot[idx];
    }
}