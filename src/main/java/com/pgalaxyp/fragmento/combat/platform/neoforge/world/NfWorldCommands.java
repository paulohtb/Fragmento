package com.pgalaxyp.fragmento.combat.platform.neoforge.world;

import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import java.util.*;
import net.minecraft.server.*;
import net.minecraft.server.level.*;
import net.minecraft.world.entity.*;

public final class NfWorldCommands implements WorldCommandPort {

    private final MinecraftServer server;

    public NfWorldCommands(MinecraftServer server) {
        this.server = Objects.requireNonNull(server);
    }

    @Override
    public void apply(FrameContext frame, GameState state, List<StateDelta> deltas) {
        for (StateDelta d : deltas) if (d instanceof DamageApplied da) applyDamage(da);
    }

    private void applyDamage(DamageApplied da) {
        ActorId targetId = da.targetActorId();
        for (ServerLevel level : server.getAllLevels()) {
            Entity entity = level.getEntity(targetId.uuid());
            if (entity != null) {
                entity.hurt(level.damageSources().generic(), da.hearts());
                return;
            }
        }
    }
}