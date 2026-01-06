package com.pgalaxyp.fragmento.rpg.platform.api.lifecycle;

import com.pgalaxyp.fragmento.rpg.core.domain.event.RpgEvent;
import com.pgalaxyp.fragmento.rpg.core.state.snapshot.CombatSnapshot;

import java.util.List;

public record PlatformFrame(
        CombatSnapshot snapshot,
        List<RpgEvent> events
) {}