package com.pgalaxyp.fragmento.combat.commandModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameCommand;
import java.util.List;

public interface CommandSourcePort {  List<FrameCommand> drain(); }