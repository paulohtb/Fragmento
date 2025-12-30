package com.pgalaxyp.fragmento.combat.old.system.channel;

import com.pgalaxyp.fragmento.combat.old.system.skill.SkillSlot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import java.util.UUID;

public final class ChannelingSession {

    private final UUID playerId;
    private final ResourceKey<Level> levelKey;
    private final SkillSlot slot;
    private final int targetEntityId;
    private final int channelEntityId;
    private final InteractionHand hand;
    private final ChannelFingerprint fingerprint;
    private final long startGameTime;
    private final int finalCooldownTicks;
    private final int cancelCooldownTicks;

    public ChannelingSession(
            UUID playerId,
            ResourceKey<Level> levelKey,
            SkillSlot slot,
            int targetEntityId,
            int channelEntityId,
            InteractionHand hand,
            ChannelFingerprint fingerprint,
            long startGameTime,
            int finalCooldownTicks,
            int cancelCooldownTicks
    ) {
        this.playerId = playerId;
        this.levelKey = levelKey;
        this.slot = slot;
        this.targetEntityId = targetEntityId;
        this.channelEntityId = channelEntityId;
        this.hand = hand;
        this.fingerprint = fingerprint;
        this.startGameTime = startGameTime;
        this.finalCooldownTicks = finalCooldownTicks;
        this.cancelCooldownTicks = cancelCooldownTicks;
    }

    public UUID playerId() {
        return playerId;
    }

    public ResourceKey<Level> levelKey() {
        return levelKey;
    }

    public SkillSlot slot() {
        return slot;
    }

    public int targetEntityId() {
        return targetEntityId;
    }

    public int channelEntityId() {
        return channelEntityId;
    }

    public InteractionHand hand() {
        return hand;
    }

    public ChannelFingerprint fingerprint() {
        return fingerprint;
    }

    public long startGameTime() {
        return startGameTime;
    }

    public int finalCooldownTicks() {
        return finalCooldownTicks;
    }

    public int cancelCooldownTicks() {
        return cancelCooldownTicks;
    }
}
