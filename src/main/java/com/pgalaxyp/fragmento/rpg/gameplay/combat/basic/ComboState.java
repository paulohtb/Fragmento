package com.pgalaxyp.fragmento.rpg.gameplay.combat.basic;

import com.pgalaxyp.fragmento.rpg.gameplay.zone.SpawnSide;

public final class ComboState {
    private int index;
    private boolean executing;
    private String activeStepId;
    private SpawnSide lastSpawnSide;

    public int index() {
        return index;
    }

    public boolean executing() {
        return executing;
    }

    public String activeStepId() {
        return activeStepId;
    }

    public SpawnSide lastSpawnSide() {
        return lastSpawnSide;
    }

    public void beginStep(int newIndex, String stepId) {
        this.index = newIndex;
        this.executing = true;
        this.activeStepId = stepId;
    }

    public void endStep() {
        this.executing = false;
        this.activeStepId = null;
    }

    public void setLastSpawnSide(SpawnSide side) {
        this.lastSpawnSide = side;
    }
}