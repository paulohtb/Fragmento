package com.pgalaxyp.fragmento.system.entity.event;

public interface SpiritSelf {

    void setAnimKey(byte key);

    void markCasted();

    void markCancelled();

    void requestDespawn();

    void requestDespawn(int delayTicks);
}