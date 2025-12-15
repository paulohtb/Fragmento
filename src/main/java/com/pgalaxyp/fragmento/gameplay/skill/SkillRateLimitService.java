package com.pgalaxyp.fragmento.gameplay.skill;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class SkillRateLimitService {

    private static final Map<UUID, EnumMap<SkillSlot, Long>> LAST_START_BY_SLOT = new HashMap<>();
    private static final Map<UUID, Long> LAST_CANCEL = new HashMap<>();

    private static final int START_INTERVAL_TICKS = 2;
    private static final int CANCEL_INTERVAL_TICKS = 1;

    private SkillRateLimitService() {
    }

    public static boolean allow(ServerPlayer player, SkillSlot slot, SkillAction action) {
        if (player == null) return false;
        if (!(player.level() instanceof ServerLevel level)) return false;

        long now = level.getGameTime();
        UUID id = player.getUUID();

        if (action == SkillAction.CANCEL) {
            Long last = LAST_CANCEL.get(id);
            if (last != null && now - last < CANCEL_INTERVAL_TICKS) return false;
            LAST_CANCEL.put(id, now);
            return true;
        }

        EnumMap<SkillSlot, Long> map = LAST_START_BY_SLOT.computeIfAbsent(id, k -> new EnumMap<>(SkillSlot.class));
        Long last = map.get(slot);
        if (last != null && now - last < START_INTERVAL_TICKS) return false;
        map.put(slot, now);
        return true;
    }

    public static void clear(ServerPlayer player) {
        if (player == null) return;
        UUID id = player.getUUID();
        LAST_START_BY_SLOT.remove(id);
        LAST_CANCEL.remove(id);
    }
}
