package com.pgalaxyp.fragmento.rpg.action.command;

import com.pgalaxyp.fragmento.rpg.action.key.*;
import java.util.*;

public record ActionStart(ActionKey actionKey) implements ActionCommand {

    public ActionStart {
        Objects.requireNonNull(actionKey, "action id cannot be null");
    }
}