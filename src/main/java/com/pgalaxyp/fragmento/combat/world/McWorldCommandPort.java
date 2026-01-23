package com.pgalaxyp.fragmento.combat.world;

import com.pgalaxyp.fragmento.combat.flow.FrameContext;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.damage.DamageApplied;
import com.pgalaxyp.fragmento.combat.flow.DomainEvent;
import java.util.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;

public final class McWorldCommandPort implements WorldCommandPort {
    private final MinecraftServer server;

    public McWorldCommandPort(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override
    public void apply(FrameContext frame, GameState state, List<DomainEvent> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(events);

        for (DomainEvent e : events) {
            if (e instanceof DamageApplied da) {
                applyDamage(da);
            }
        }
    }

    private void applyDamage(DamageApplied da) {
        UUID uuid = da.targetActorId().uuid();
        LivingEntity e = findLiving(uuid);
        if (e == null) return;

        float amount = da.hearts() * 2.0f;
        e.setHealth(Math.max(0.0f, e.getHealth() - amount));
    }

    private LivingEntity findLiving(UUID uuid) {
        Entity player = server.getPlayerList().getPlayer(uuid);
        if (player instanceof LivingEntity le) return le;

        for (ServerLevel level : server.getAllLevels()) {
            Entity e = level.getEntity(uuid);
            if (e instanceof LivingEntity le) return le;
        }
        return null;
    }
}