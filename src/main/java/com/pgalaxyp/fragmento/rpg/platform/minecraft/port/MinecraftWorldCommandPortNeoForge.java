package com.pgalaxyp.fragmento.rpg.platform.minecraft.port;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.event.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.ActorState;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.ids.MinecraftActorIds;
import com.pgalaxyp.fragmento.rpg.port.WorldCommandPort;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import java.util.List;
import java.util.Objects;

public final class MinecraftWorldCommandPortNeoForge implements WorldCommandPort {

    private final MinecraftServer server;

    public MinecraftWorldCommandPortNeoForge(MinecraftServer server) {
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
            ActorId id = MinecraftActorIds.fromUuid(p.getUUID());
            ActorState s = state.actors().get(id);
            if (s == null) {
                continue;
            }
            double max = s.maxHealthHearts() * 2;
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
        for (ServerLevel level : server.getAllLevels()) {
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(
                    -3.0E7, -2.0E4, -3.0E7,
                    3.0E7, 2.0E4, 3.0E7
            ));
            for (LivingEntity e : entities) {
                if (e == null) {
                    continue;
                }
                if (MinecraftActorIds.fromUuid(e.getUUID()).equals(actorId)) {
                    return e;
                }
            }
        }
        return null;
    }
}