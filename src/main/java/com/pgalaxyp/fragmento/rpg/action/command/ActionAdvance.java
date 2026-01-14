package com.pgalaxyp.fragmento.rpg.action.command;

import com.pgalaxyp.fragmento.rpg.combo.api.*;
import java.util.*;

public record ActionAdvance(ComboInput input) implements ActionCommand {
    public ActionAdvance {
        Objects.requireNonNull(input, "input cannot be null");
    }
}