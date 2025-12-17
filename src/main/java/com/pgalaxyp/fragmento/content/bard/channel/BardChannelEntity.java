package com.pgalaxyp.fragmento.content.bard.channel;

import com.pgalaxyp.fragmento.content.bard.entity.BardSkillEntityBase;
import com.pgalaxyp.fragmento.system.channel.ChannelEntity;
import net.minecraft.server.level.ServerLevel;

public final class BardChannelEntity implements ChannelEntity {

    private final BardSkillEntityBase spirit;

    public BardChannelEntity(BardSkillEntityBase spirit) {
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
