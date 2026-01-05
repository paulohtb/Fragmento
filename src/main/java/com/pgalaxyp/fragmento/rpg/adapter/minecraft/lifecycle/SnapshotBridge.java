package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.adapter.minecraft.context.ActorContextServer;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.effects.EffectRepository;
import java.util.Objects;
import com.pgalaxyp.fragmento.rpg.gameplay.state.CombatDirty;
import com.pgalaxyp.fragmento.rpg.network.payload.s2c.CombatStateSnapshotPayload;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.CombatSnapshot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class SnapshotBridge {

    private final ActorContextServer context;
    private final CombatSnapshotService snapshots;
    private final TickBus bus;

    private volatile boolean pending;
    private volatile long pendingActorId;
    private volatile long pendingVersion;

    public SnapshotBridge(ActorContextServer context, ActorRepository actors, EffectRepository effects, TickBus bus) {
        this.context = Objects.requireNonNull(context);
        this.snapshots = new CombatSnapshotService(Objects.requireNonNull(actors), Objects.requireNonNull(effects));
        this.bus = Objects.requireNonNull(bus);

        bus.subscribe(CombatDirty.class, this::onDirty);
    }

    private void onDirty(CombatDirty e) {
        pending = true;
        pendingActorId = e.actorId();
        pendingVersion = e.version();
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        if (!pending) return;

        pending = false;

        var sp = context.player(pendingActorId).orElse(null);
        if (sp == null) return;

        CombatSnapshot snap = snapshots.build(pendingActorId, pendingVersion);
        PacketDistributor.sendToPlayer(sp, new CombatStateSnapshotPayload(snap));
    }
}