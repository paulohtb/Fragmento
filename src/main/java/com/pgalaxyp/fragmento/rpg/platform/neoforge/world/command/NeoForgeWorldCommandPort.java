package com.pgalaxyp.fragmento.rpg.platform.neoforge.world.command;

import com.pgalaxyp.fragmento.rpg.core.content.GameContent;
import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.time.FrameContext;
import com.pgalaxyp.fragmento.rpg.core.events.delta.DamageApplied;
import com.pgalaxyp.fragmento.rpg.core.events.delta.StateDelta;
import com.pgalaxyp.fragmento.rpg.core.state.GameState;
import com.pgalaxyp.fragmento.rpg.ports.WorldCommandPort;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

public final class NeoForgeWorldCommandPort implements WorldCommandPort {

    private final MinecraftServer server;

    public NeoForgeWorldCommandPort(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }
        this.server = server;
    }

    @Override
    public void apply(
            FrameContext frame,
            GameState state,
            GameContent content,
            List<StateDelta> deltas
    ) {
        for (StateDelta d : deltas) {
            if (d instanceof DamageApplied da) {
                applyDamage(state, da);
            }
        }
    }

    private void applyDamage(GameState state, DamageApplied da) {
        ActorId targetId = da.targetActorId();

        for (ServerLevel level : server.getAllLevels()) {
            Entity e = level.getEntity(targetId.uuid());
            if (e != null) {
                e.hurt(level.damageSources().generic(), da.hearts());
                return;
            }
        }
    }
}