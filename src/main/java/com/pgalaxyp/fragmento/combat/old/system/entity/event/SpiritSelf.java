package com.pgalaxyp.fragmento.combat.old.system.entity.event;

public interface SpiritSelf {

    void setAnimKey(byte key);

    void markCasted();

    void markCancelled();

    void requestDespawn();

    void requestDespawn(int delayTicks);
}