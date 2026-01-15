package com.pgalaxyp.fragmento.combat.platform.neoforge.world;

import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.content.*;
import com.pgalaxyp.fragmento.combat.core.time.*;
import com.pgalaxyp.fragmento.combat.core.state.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.delta.*;
import java.util.*;
import net.minecraft.server.*;
import net.minecraft.world.entity.*;
import net.minecraft.server.level.*;

public final class NfWorldCommands implements WorldCommandPort {

    private final MinecraftServer server;

    public NfWorldCommands(MinecraftServer server) {
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