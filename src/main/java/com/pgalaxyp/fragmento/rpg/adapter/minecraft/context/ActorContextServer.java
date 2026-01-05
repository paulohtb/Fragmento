package com.pgalaxyp.fragmento.rpg.adapter.minecraft.context;

import com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle.ActorIds;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class ActorContextServer {

    private final ActorIds actorIds;

    public ActorContextServer(ActorIds actorIds) {
        this.actorIds = Objects.requireNonNull(actorIds);
    }

    public Optional<ServerPlayer> player(long actorId) {
        var uuid = actorIds.uuidOf(actorId).orElse(null);
        if (uuid == null) return Optional.empty();

        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return Optional.empty();

        return Optional.ofNullable(server.getPlayerList().getPlayer(uuid));
    }

    public Optional<ServerLevel> level(long actorId) {
        return player(actorId).map(ServerPlayer::serverLevel);
    }
}