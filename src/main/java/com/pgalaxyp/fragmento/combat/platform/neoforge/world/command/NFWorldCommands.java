package com.pgalaxyp.fragmento.combat.platform.neoforge.world.command;

import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.domain.ids.*;
import com.pgalaxyp.fragmento.combat.core.events.delta.*;
import java.util.*;
import net.minecraft.server.*;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.*;

public final class NFWorldCommands implements WorldCommandPort {

    private final MinecraftServer server;

    public NFWorldCommands(MinecraftServer server) {
        if (server == null) {
            throw new IllegalArgumentException();
        }
        this.server = server;
    }

    @Override
    public void apply(FrameContext frame, GameState state, GameContent content, List<StateDelta> deltas) {
        for (StateDelta d : deltas) {
            if (d instanceof DamageApplied da) { applyDamage(state, da); }
        }
    }

    private void applyDamage(GameState state, DamageApplied da) {
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