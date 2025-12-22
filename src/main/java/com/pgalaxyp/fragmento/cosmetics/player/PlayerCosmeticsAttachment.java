package com.pgalaxyp.fragmento.cosmetics.player;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticId;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticLoadout;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticSlot;
import com.pgalaxyp.fragmento.cosmetics.api.PlayerCosmeticState;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.slf4j.Logger;

public final class PlayerCosmeticsAttachment implements INBTSerializable<CompoundTag> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private PlayerCosmeticState state;

    public PlayerCosmeticsAttachment() {
        this.state = PlayerCosmeticState.empty();
    }

    public PlayerCosmeticState state() {
        return state;
    }

    public void setState(PlayerCosmeticState state) {
        this.state = Objects.requireNonNull(state, "state");
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("version", state.version());
        tag.put("base", writeLoadout(state.baseLoadout()));
        tag.put("forced", writeLoadout(state.forcedLoadout()));
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) {
            state = PlayerCosmeticState.empty();
            return;
        }
        long version = nbt.getLong("version");
        CosmeticLoadout base = readLoadout(nbt.getCompound("base"));
        CosmeticLoadout forced = readLoadout(nbt.getCompound("forced"));
        state = new PlayerCosmeticState(base, forced, version);
        LOGGER.info("PlayerCosmeticsAttachment deserialize ok version {} base {} forced {}", Long.valueOf(version), Integer.valueOf(base.equippedView().size()), Integer.valueOf(forced.equippedView().size()));
    }

    private static CompoundTag writeLoadout(CosmeticLoadout loadout) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Map.Entry<CosmeticSlot, CosmeticId> e : loadout.equippedView().entrySet()) {
            CompoundTag entry = new CompoundTag();
            entry.putInt("slot", e.getKey().ordinal());
            entry.putString("id", e.getValue().value().toString());
            list.add(entry);
        }
        tag.put("entries", list);
        return tag;
    }

    private static CosmeticLoadout readLoadout(CompoundTag tag) {
        if (tag == null) {
            return CosmeticLoadout.EMPTY;
        }
        ListTag list = tag.getList("entries", Tag.TAG_COMPOUND);
        EnumMap<CosmeticSlot, CosmeticId> map = new EnumMap<>(CosmeticSlot.class);
        CosmeticSlot[] slots = CosmeticSlot.values();
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            int ord = entry.getInt("slot");
            if (ord < 0 || ord >= slots.length) {
                continue;
            }
            ResourceLocation rl = ResourceLocation.tryParse(entry.getString("id"));
            if (rl == null) {
                continue;
            }
            map.put(slots[ord], CosmeticId.of(rl));
        }
        return new CosmeticLoadout(map);
    }
}