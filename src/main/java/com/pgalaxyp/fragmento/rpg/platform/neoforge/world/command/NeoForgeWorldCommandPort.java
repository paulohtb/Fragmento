package com.pgalaxyp.fragmento.rpg.platform.neoforge.world.command;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.ports.WorldCommandPort;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

public final class NeoForgeWorldCommandPort implements WorldCommandPort {

    private final MinecraftServer server;

    public NeoForgeWorldCommandPort(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }
        this.server = server;
    }

    @Override
    public void apply(FrameContext frame, GameState state, RpgContent content, List<StateDelta> deltas) {
        if (frame == null || state == null || content == null || deltas == null) {
            throw new IllegalArgumentException();
        }

        for (ServerPlayer p : server.getPlayerList().getPlayers()) {
            ActorId id = new ActorId(p.getUUID());
            ActorState s = state.actors().get(id);
            if (s == null) {
                continue;
            }
            double max = s.maxHealthHearts() * 2.0;
            if (p.getAttribute(Attributes.MAX_HEALTH) != null) {
                Objects.requireNonNull(p.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(max);
            }
            float hp = (float) (s.healthHearts() * 2);
            if (hp < 0f) {
                hp = 0f;
            }
            p.setHealth(hp);
        }

        for (StateDelta d : deltas) {
            if (d instanceof DamageApplied(ActorId target, int hearts)) {
                if (state.actors().containsKey(target)) {
                    continue;
                }
                LivingEntity e = findLivingByActorId(target);
                if (e == null) {
                    continue;
                }
                float amount = (float) (hearts * 2);
                e.hurt(e.damageSources().magic(), amount);
            }
        }
    }

    private LivingEntity findLivingByActorId(ActorId actorId) {
        if (actorId == null) {
            return null;
        }

        UUID uuid = actorId.uuid();
        ServerPlayer p = server.getPlayerList().getPlayer(uuid);
        if (p != null) {
            return p;
        }

        for (ServerLevel level : server.getAllLevels()) {
            Entity e = level.getEntity(uuid);
            if (e instanceof LivingEntity le) {
                return le;
            }
        }

        return null;
    }
}