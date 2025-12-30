package com.pgalaxyp.fragmento.combat.engine.adapter;

import com.pgalaxyp.fragmento.combat.engine.time.TickSource;
import net.minecraft.world.level.Level;

public final class MinecraftTickSource implements TickSource {

    private final Level level;

    public MinecraftTickSource(Level level) {
        this.level = level;
    }

    @Override
    public long gameTick() {
        if (level == null) {
            return 0L;
        }
        return level.getGameTime();
    }
}