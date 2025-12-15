package com.pgalaxyp.fragmento.gameplay.channel;

import net.minecraft.server.level.ServerLevel;

public interface ChannelEntity {

    boolean isAlive();

    boolean isCasted();

    void markCasted();

    void discard(ServerLevel level);
}
