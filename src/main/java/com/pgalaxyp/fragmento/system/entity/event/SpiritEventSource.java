package com.pgalaxyp.fragmento.system.entity.event;

public interface SpiritEventSource {

    boolean consumeCasted();

    boolean consumeCancelled();
}