package com.pgalaxyp.fragmento.combat.engine.time;

import net.minecraft.world.level.Level;

public final class LevelTickSource implements TickSource {

    private final Level level;

    public LevelTickSource(Level level) {
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