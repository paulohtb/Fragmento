package com.pgalaxyp.fragmento.rpg.action.runtime;

import com.pgalaxyp.fragmento.rpg.action.id.*;
import java.util.*;

public record StartAction(ActionId actionId) implements ActionCommand {

    public StartAction {
        Objects.requireNonNull(actionId);
    }
}