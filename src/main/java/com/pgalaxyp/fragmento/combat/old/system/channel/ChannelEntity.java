package com.pgalaxyp.fragmento.combat.old.system.channel;

import net.minecraft.server.level.ServerLevel;

public interface ChannelEntity {

    boolean isAlive();

    boolean isCasted();

    void markCasted();

    void discard(ServerLevel level);
}
