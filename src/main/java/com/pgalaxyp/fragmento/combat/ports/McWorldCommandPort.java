package com.pgalaxyp.fragmento.combat.ports;

import com.pgalaxyp.fragmento.combat.core.time.FrameContext;
import com.pgalaxyp.fragmento.combat.core.state.GameState;
import com.pgalaxyp.fragmento.combat.delta.DamageApplied;
import com.pgalaxyp.fragmento.combat.delta.StateDelta;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public final class McWorldCommandPort implements WorldCommandPort {
    private final MinecraftServer server;

    public McWorldCommandPort(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override
    public void apply(FrameContext frame, GameState state, List<StateDelta> deltas) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(state);
        Objects.requireNonNull(deltas);

        for (StateDelta d : deltas) {
            if (d instanceof DamageApplied da) {
                applyDamage(da);
            }
        }
    }

    private void applyDamage(DamageApplied da) {
        UUID uuid = da.targetActorId().uuid();
        LivingEntity e = findLiving(uuid);
        if (e == null) return;

        float amount = da.hearts() * 2.0f;
        float next = e.getHealth() - amount;
        if (next < 0.0f) next = 0.0f;

        e.setHealth(next);
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