package com.pgalaxyp.fragmento.rpg.ports;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.query.ExternalQueryEvent;
import com.pgalaxyp.fragmento.rpg.core.events.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import java.util.List;

public interface WorldQueryPort {
    List<DomainResolution> resolve(FrameContext frame, GameState state, RpgContent content, List<ExternalQueryEvent> queries);
}