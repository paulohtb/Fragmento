package com.pgalaxyp.fragmento.combat.commandModule.api;

import com.pgalaxyp.fragmento.combat.frameModule.api.FrameCommand;

public interface CommandSinkPort {  boolean enqueue(FrameCommand command); }