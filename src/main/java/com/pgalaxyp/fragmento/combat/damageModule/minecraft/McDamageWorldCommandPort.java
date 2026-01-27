package com.pgalaxyp.fragmento.combat.damageModule.minecraft;

import com.pgalaxyp.fragmento.combat.random.*;
import com.pgalaxyp.fragmento.combat.damageModule.event.DamageApplied;
import java.util.*;
import net.minecraft.world.entity.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;

public final class McDamageWorldCommandPort implements WorldCommandPort {
    private final MinecraftServer server;

    public McDamageWorldCommandPort(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override
    public void apply(FrameContext frame, GameState state, List<FrameEvent> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(events);

        for (FrameEvent event : events) {
            if (event instanceof DamageApplied damage) applyDamage(damage);
        }
    }

    private void applyDamage(DamageApplied damage) {
        UUID uuid = damage.targetActorId().value();
        LivingEntity entity = findLiving(uuid);
        if (entity == null) return;

        float amount = damage.hearts() * 2.0f;
        entity.setHealth(Math.max(0.0f, entity.getHealth() - amount));
    }

    private LivingEntity findLiving(UUID uuid) {
        Entity player = server.getPlayerList().getPlayer(uuid);
        if (player instanceof LivingEntity entity) return entity;

        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(uuid);
            if (entity instanceof LivingEntity living) return living;
        }

        return null;
    }
}