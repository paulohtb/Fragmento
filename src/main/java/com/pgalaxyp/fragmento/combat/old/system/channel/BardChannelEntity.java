package com.pgalaxyp.fragmento.combat.old.system.channel;

import com.pgalaxyp.fragmento.combat.old.system.entity.host.NewwSpiritEntityBase;
import net.minecraft.server.level.ServerLevel;

public final class BardChannelEntity implements ChannelEntity {

    private final NewwSpiritEntityBase spirit;

    public BardChannelEntity(NewwSpiritEntityBase spirit) {
        this.spirit = spirit;
    }

    @Override
    public boolean isAlive() {
        return spirit.isAlive();
    }

    @Override
    public boolean isCasted() {
        return spirit.isCasted();
    }

    @Override
    public void markCasted() {
        spirit.markCasted();
    }

    @Override
    public void discard(ServerLevel level) {
        spirit.requestDespawn();
    }
}