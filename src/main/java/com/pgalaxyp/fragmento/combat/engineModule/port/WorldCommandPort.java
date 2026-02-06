package com.pgalaxyp.fragmento.combat.engineModule.port;

import com.pgalaxyp.fragmento.combat.frameModule.api.*;
import java.util.List;

public interface WorldCommandPort { void apply(FrameContext frame, List<FrameEvent> events); }