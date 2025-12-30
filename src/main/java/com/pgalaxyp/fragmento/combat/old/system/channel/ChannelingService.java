package com.pgalaxyp.fragmento.combat.old.system.channel;

import com.pgalaxyp.fragmento.combat.old.system.gameplay.cooldown.PlayerSkillCooldownService;
import com.pgalaxyp.fragmento.combat.old.system.skill.SkillSlot;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public final class ChannelingService {

    public static final int REQUIRED_TICKS = 40;

    private static final int CANCEL_PENDING_MAX_AGE_TICKS = 4;
    private static final int SESSION_MAX_AGE_TICKS = 200;

    private static final Map<UUID, ChannelingSession> ACTIVE_BY_PLAYER = new HashMap<>();
    private static final Map<ResourceKey<Level>, Map<UUID, ChannelingSession>> ACTIVE_BY_LEVEL = new HashMap<>();
    private static final Map<UUID, Long> PENDING_CANCEL_UNTIL = new HashMap<>();

    private static ChannelAdapter adapter;

    private ChannelingService() {}

    public static void registerAdapter(ChannelAdapter channelAdapter) {
        adapter = channelAdapter;
    }

    public static void clearAllServer() {
        ACTIVE_BY_PLAYER.clear();
        ACTIVE_BY_LEVEL.clear();
        PENDING_CANCEL_UNTIL.clear();
    }

    public static boolean hasActive(ServerPlayer player) {
        return player != null && ACTIVE_BY_PLAYER.containsKey(player.getUUID());
    }

    public static boolean consumePendingCancel(ServerPlayer player) {
        if (player == null) return false;
        if (!(player.level() instanceof ServerLevel level)) return false;

        UUID id = player.getUUID();
        Long until = PENDING_CANCEL_UNTIL.remove(id);
        if (until == null) return false;

        return until >= level.getGameTime();
    }

    public static void clearPlayer(ServerPlayer player) {
        if (player == null) return;

        ChannelingSession session = ACTIVE_BY_PLAYER.remove(player.getUUID());
        if (session == null) return;

        removeFromLevelIndex(session);
        cancelInternal(player, session, true);
    }

    public static void clearAllForLevel(ServerLevel level) {
        if (level == null) return;

        Map<UUID, ChannelingSession> byLevel = ACTIVE_BY_LEVEL.remove(level.dimension());
        if (byLevel == null) return;

        for (ChannelingSession s : byLevel.values()) {
            ACTIVE_BY_PLAYER.remove(s.playerId());
            discardEntityOnly(level, s);
        }
    }

    public static void start(
            ServerPlayer player,
            SkillSlot slot,
            int targetEntityId,
            int channelEntityId,
            InteractionHand hand,
            ItemStack stack,
            int finalCooldownTicks,
            int cancelCooldownTicks
    ) {
        if (adapter == null) return;
        if (!(player.level() instanceof ServerLevel level)) return;

        UUID id = player.getUUID();
        if (ACTIVE_BY_PLAYER.containsKey(id)) return;

        ChannelEntity entity = adapter.resolveEntity(level, channelEntityId);
        if (entity == null || !entity.isAlive()) return;

        ChannelFingerprint fingerprint = adapter.createFingerprint(stack);
        if (fingerprint == null) return;

        long now = level.getGameTime();

        ChannelingSession session = new ChannelingSession(
                id,
                level.dimension(),
                slot,
                targetEntityId,
                channelEntityId,
                hand,
                fingerprint,
                now,
                finalCooldownTicks,
                cancelCooldownTicks
        );

        ACTIVE_BY_PLAYER.put(id, session);
        ACTIVE_BY_LEVEL.computeIfAbsent(level.dimension(), k -> new HashMap<>()).put(id, session);
    }

    public static void cancel(ServerPlayer player) {
        if (player == null) return;

        ChannelingSession session = ACTIVE_BY_PLAYER.remove(player.getUUID());
        if (session == null) {
            if (player.level() instanceof ServerLevel level) {
                PENDING_CANCEL_UNTIL.put(player.getUUID(), level.getGameTime() + CANCEL_PENDING_MAX_AGE_TICKS);
            }
            return;
        }

        removeFromLevelIndex(session);
        cancelInternal(player, session, true);
    }

    public static void tick(ServerLevel level) {
        if (level == null || adapter == null) return;

        Map<UUID, ChannelingSession> byLevel = ACTIVE_BY_LEVEL.get(level.dimension());
        if (byLevel == null) return;

        long now = level.getGameTime();
        Iterator<ChannelingSession> it = byLevel.values().iterator();

        while (it.hasNext()) {
            ChannelingSession s = it.next();
            long age = now - s.startGameTime();

            if (age > SESSION_MAX_AGE_TICKS) {
                it.remove();
                ACTIVE_BY_PLAYER.remove(s.playerId());
                discardEntityOnly(level, s);
                continue;
            }

            Player raw = level.getPlayerByUUID(s.playerId());
            if (!(raw instanceof ServerPlayer player) || !player.isAlive()) {
                it.remove();
                ACTIVE_BY_PLAYER.remove(s.playerId());
                discardEntityOnly(level, s);
                continue;
            }

            if (!adapter.isStillHolding(player, s.hand(), s.fingerprint())) {
                it.remove();
                ACTIVE_BY_PLAYER.remove(s.playerId());
                cancelInternal(player, s, true);
                continue;
            }

            ChannelEntity entity = adapter.resolveEntity(level, s.channelEntityId());
            if (entity == null || !entity.isAlive()) {
                it.remove();
                ACTIVE_BY_PLAYER.remove(s.playerId());
                continue;
            }

            if (age < REQUIRED_TICKS) continue;

            entity.markCasted();

            int cd = Math.max(0, s.finalCooldownTicks());
            if (cd > 0) {
                PlayerSkillCooldownService.apply(player, s.slot(), cd);
                adapter.applyVisualCooldown(player, cd);
            }

            it.remove();
            ACTIVE_BY_PLAYER.remove(s.playerId());
        }

        if (byLevel.isEmpty()) {
            ACTIVE_BY_LEVEL.remove(level.dimension());
        }
    }

    private static void cancelInternal(ServerPlayer player, ChannelingSession s, boolean applyCd) {
        if (!(player.level() instanceof ServerLevel level)) return;

        ChannelEntity e = adapter.resolveEntity(level, s.channelEntityId());
        if (e != null && e.isAlive() && !e.isCasted()) {
            e.discard(level);
        }

        if (applyCd) {
            int cd = Math.max(0, s.cancelCooldownTicks());
            if (cd > 0) {
                PlayerSkillCooldownService.apply(player, s.slot(), cd);
                adapter.applyVisualCooldown(player, cd);
            }
        }
    }

    private static void discardEntityOnly(ServerLevel level, ChannelingSession s) {
        ChannelEntity e = adapter.resolveEntity(level, s.channelEntityId());
        if (e != null && e.isAlive() && !e.isCasted()) {
            e.discard(level);
        }
    }

    private static void removeFromLevelIndex(ChannelingSession s) {
        Map<UUID, ChannelingSession> map = ACTIVE_BY_LEVEL.get(s.levelKey());
        if (map != null) {
            map.remove(s.playerId());
            if (map.isEmpty()) ACTIVE_BY_LEVEL.remove(s.levelKey());
        }
    }
}
