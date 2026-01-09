package com.pgalaxyp.fragmento.rpg.platform.minecraft;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.query.ExternalQueryEvent;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.port.WorldQueryPort;
import java.util.List;

public final class MinecraftWorldQueryPortStub implements WorldQueryPort {

    @Override
    public List<DomainResolution> resolve(FrameContext frame, GameState state, RpgContent content, List<ExternalQueryEvent> queries) {
        if (frame == null || state == null || content == null || queries == null) {
            throw new IllegalArgumentException();
        }
        throw new UnsupportedOperationException();
    }
}