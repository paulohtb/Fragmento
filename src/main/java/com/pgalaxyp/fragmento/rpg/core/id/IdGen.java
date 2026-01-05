package com.pgalaxyp.fragmento.rpg.core.id;

public final class IdGen {
    private long next;

    public IdGen(long seed) {
        this.next = seed;
    }

    public long next() {
        return ++next;
    }
}