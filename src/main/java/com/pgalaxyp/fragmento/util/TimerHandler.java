package com.pgalaxyp.fragmento.util;

import com.pgalaxyp.fragmento.entity.timerEntity.TimerEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TimerHandler {

    private static final class TimerData {
        int value = 0;
        long lastInteraction = 0;
    }

    private static final Map<LivingEntity, TimerData> ENTITY_TIMERS = new ConcurrentHashMap<>();
    private static final Map<LivingEntity, TimerEntity> INDICATOR_ENTITIES = new ConcurrentHashMap<>();

    private static final int DECAY_DELAY = 20;
    private static final int DECAY_RATE = 1;

    public static void incrementTimer(LivingEntity entity) {
        TimerData data = ENTITY_TIMERS.computeIfAbsent(entity, k -> new TimerData());

        data.value += 1;
        data.lastInteraction = entity.level().getGameTime();

        updateIndicator(entity);
    }

    public static void tickAll(ServerLevel level) {
        long currentTime = level.getGameTime();

        List<LivingEntity> toRemove = new ArrayList<>();
        List<LivingEntity> toUpdate = new ArrayList<>();

        ENTITY_TIMERS.forEach((entity, data) -> {
            if (!entity.isAlive()) {
                toRemove.add(entity);
                return;
            }

            // Apply decay
            if (currentTime - data.lastInteraction > DECAY_DELAY) {
                data.value = Math.max(0, data.value - DECAY_RATE);
                data.lastInteraction = currentTime;
            }

            if (data.value == 0 && currentTime - data.lastInteraction > DECAY_DELAY) {
                toRemove.add(entity);
            } else if (data.value > 0) {
                toUpdate.add(entity);
            }
        });

        toRemove.forEach(entity -> {
            ENTITY_TIMERS.remove(entity);
            removeIndicator(entity);
        });

        toUpdate.forEach(TimerHandler::updateIndicator);

        // Clean up dead indicators
        INDICATOR_ENTITIES.entrySet().removeIf(entry ->
                !entry.getKey().isAlive() ||
                        !entry.getValue().isAlive() ||
                        !hasTimer(entry.getKey())
        );
    }

    public static void removeTimer(LivingEntity entity) {
        ENTITY_TIMERS.remove(entity);
        removeIndicator(entity);
    }

    private static void removeIndicator(LivingEntity entity) {
        TimerEntity indicator = INDICATOR_ENTITIES.remove(entity);
        if (indicator != null && indicator.isAlive()) {
            indicator.discard();
        }
    }

    private static void updateIndicator(LivingEntity entity) {
        if (!(entity.level() instanceof ServerLevel serverLevel)) return;
        if (getTimerValue(entity) <= 0) return;

        // Check if indicator already exists
        TimerEntity indicator = INDICATOR_ENTITIES.get(entity);

        if (indicator == null || !indicator.isAlive()) {
            // Create new only if necessary
            indicator = new TimerEntity(serverLevel, entity);
            serverLevel.addFreshEntity(indicator);
            INDICATOR_ENTITIES.put(entity, indicator);
        }

        // Update text (for both new and existing indicators)
        long currentTime = serverLevel.getGameTime();
        long timeSinceLastInteraction = currentTime - ENTITY_TIMERS.get(entity).lastInteraction;
        long timeUntilDecay = Math.max(0, DECAY_DELAY - timeSinceLastInteraction);
        String displayText = String.format("⏱ %d (↻ %d)",
                getTimerValue(entity),
                timeUntilDecay);

        indicator.setCustomName(Component.literal(displayText));
        indicator.setCustomNameVisible(true);

        // Position the indicator above the entity
        double expectedY = entity.getY() + entity.getBbHeight() + 0.5;
        indicator.setPos(entity.getX(), expectedY, entity.getZ());
    }

    public static boolean hasTimer(LivingEntity entity) {
        TimerData data = ENTITY_TIMERS.get(entity);
        return data != null && data.value > 0;
    }

    public static int getTimerValue(LivingEntity entity) {
        TimerData data = ENTITY_TIMERS.get(entity);
        return data != null ? data.value : 0;
    }

    public static boolean isReadyToTrigger(LivingEntity entity) {
        return getTimerValue(entity) >= 10;
    }

    public static int resetTimer(LivingEntity entity) {
        TimerData data = ENTITY_TIMERS.get(entity);
        if (data == null) return 0;

        int previousValue = data.value;
        data.value = 0;
        removeIndicator(entity);
        return previousValue;
    }
}