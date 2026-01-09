package com.pgalaxyp.fragmento.rpg.platform.minecraft;

import com.pgalaxyp.fragmento.rpg.core.content.RpgContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.domain.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.event.query.ExternalQueryEvent;
import com.pgalaxyp.fragmento.rpg.core.event.query.TargetingQueryRequested;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.DomainResolution;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.TargetingCandidate;
import com.pgalaxyp.fragmento.rpg.core.event.resolution.TargetingQueryResolved;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.port.WorldQueryPort;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class MinecraftWorldQueryPortStub implements WorldQueryPort {

    @Override
    public List<DomainResolution> resolve(FrameContext frame, GameState state, RpgContent content, List<ExternalQueryEvent> queries) {
        if (frame == null || state == null || content == null || queries == null) {
            throw new IllegalArgumentException();
        }

        List<DomainResolution> out = new ArrayList<>();

        Comparator<TargetingCandidate> byActorId = Comparator.comparing(
                TargetingCandidate::actorId,
                Comparator.nullsFirst(Comparator.naturalOrder())
        );

        for (ExternalQueryEvent q : queries) {
            if (q == null) {
                throw new IllegalArgumentException();
            }
            if (q instanceof TargetingQueryRequested tr) {
                List<TargetingCandidate> candidates = new ArrayList<>();
                ActorId source = tr.sourceActorId();
                for (ActorId id : state.actors().keySet()) {
                    if (id == null || id.equals(source)) {
                        continue;
                    }
                    candidates.add(new TargetingCandidate(id, 0));
                }
                candidates.sort(byActorId);
                out.add(new TargetingQueryResolved(tr.queryId(), candidates));
            }
        }

        return List.copyOf(out);
    }
}