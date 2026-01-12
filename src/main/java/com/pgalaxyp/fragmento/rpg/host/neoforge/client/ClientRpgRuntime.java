package com.pgalaxyp.fragmento.rpg.host.neoforge.client;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.host.api.LocalActorProvider;
import java.util.Optional;

public final class ClientRpgRuntime implements LocalActorProvider {

    private Optional<ActorId> localActorId = Optional.empty();

    @Override
    public Optional<ActorId> localActorId() {
        return localActorId;
    }

    public void setLocalActorId(ActorId actorId) {
        this.localActorId = Optional.ofNullable(actorId);
    }

    public void clearLocalActorId() {
        this.localActorId = Optional.empty();
    }
}