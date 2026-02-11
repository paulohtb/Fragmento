package com.pgalaxyp.fragmento.combat.platformModule.minecraft;

import com.pgalaxyp.fragmento.combat.engineModule.port.WorldCommandPort;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameContext;
import com.pgalaxyp.fragmento.combat.frameModule.api.FrameEvent;
import com.pgalaxyp.fragmento.combat.projectileModule.event.ProjectileSpawned;
import com.pgalaxyp.fragmento.combat.projectileModule.port.ProjectileWorldCommandPort;
import java.util.List;
import java.util.Objects;

public record McProjectileWorldCommandAdapter(ProjectileWorldCommandPort projectiles) implements WorldCommandPort {
    public McProjectileWorldCommandAdapter {
        Objects.requireNonNull(projectiles);
    }

    @Override public void apply(FrameContext frame, List<FrameEvent> events) {
        Objects.requireNonNull(frame);
        Objects.requireNonNull(events);
        for (var e : events) if (e instanceof ProjectileSpawned p) projectiles.apply(p);
    }
}
