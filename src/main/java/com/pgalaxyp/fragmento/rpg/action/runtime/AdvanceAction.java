package com.pgalaxyp.fragmento.rpg.action.runtime;

public record AdvanceAction(int stepIndex) implements ActionCommand {

    public AdvanceAction {
        if (stepIndex < 0) {
            throw new IllegalArgumentException();
        }
    }
}