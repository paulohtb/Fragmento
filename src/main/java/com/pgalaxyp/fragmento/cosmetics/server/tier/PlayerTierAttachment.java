package com.pgalaxyp.fragmento.cosmetics.server.tier;

import com.mojang.logging.LogUtils;
import com.pgalaxyp.fragmento.cosmetics.api.CosmeticTier;
import java.util.Objects;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.slf4j.Logger;

public final class PlayerTierAttachment implements INBTSerializable<CompoundTag> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private CosmeticTier tier;
    private long version;
    private long lastSuccessSyncMillis;
    private long lastAttemptMillis;
    private long lastCommandMillis;

    public PlayerTierAttachment() {
        this.tier = CosmeticTier.TIER_0;
        this.version = 0L;
        this.lastSuccessSyncMillis = 0L;
        this.lastAttemptMillis = 0L;
        this.lastCommandMillis = 0L;
    }

    public CosmeticTier tier() {
        return tier;
    }

    public long version() {
        return version;
    }

    public long lastSuccessSyncMillis() {
        return lastSuccessSyncMillis;
    }

    public long lastAttemptMillis() {
        return lastAttemptMillis;
    }

    public void touchAttempt(long nowMillis) {
        lastAttemptMillis = nowMillis;
    }

    public void touchSuccess(long nowMillis) {
        lastSuccessSyncMillis = nowMillis;
    }

    public boolean consumeCommandCooldown(long nowMillis, long cooldownMillis) {
        long last = lastCommandMillis;
        if (nowMillis < last) {
            lastCommandMillis = nowMillis;
            return true;
        }
        long elapsed = nowMillis - last;
        if (elapsed < cooldownMillis) return false;
        lastCommandMillis = nowMillis;
        return true;
    }

    public boolean applyTier(CosmeticTier tier, long nowMillis) {
        CosmeticTier next = Objects.requireNonNull(tier, "tier");
        touchSuccess(nowMillis);
        if (this.tier == next) return false;
        this.tier = next;
        this.version++;
        return true;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("tier", tier.level());
        tag.putLong("version", version);
        tag.putLong("lastSuccess", lastSuccessSyncMillis);
        tag.putLong("lastAttempt", lastAttemptMillis);
        tag.putLong("lastCmd", lastCommandMillis);
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        if (nbt == null) return;
        tier = CosmeticTier.fromLevel(nbt.getInt("tier"));
        version = nbt.getLong("version");
        lastSuccessSyncMillis = nbt.getLong("lastSuccess");
        lastAttemptMillis = nbt.getLong("lastAttempt");
        lastCommandMillis = nbt.getLong("lastCmd");
        LOGGER.info("PlayerTierAttachment deserialize tier {} version {}", tier.name(), Long.valueOf(version));
    }
}