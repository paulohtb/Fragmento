package com.pgalaxyp.fragmento.rpg_old.domain.timing;

public record Duration(long ticks) {

    public static Duration ofTicks(long ticks) {
        return new Duration(ticks);
    }
}