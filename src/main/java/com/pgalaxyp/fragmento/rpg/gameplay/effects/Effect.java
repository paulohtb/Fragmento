package com.pgalaxyp.fragmento.rpg.gameplay.effects;

public interface Effect {
    EffectId id();
    void start(EffectWorld world);
    boolean update(EffectWorld world);
    void end(EffectWorld world);
}