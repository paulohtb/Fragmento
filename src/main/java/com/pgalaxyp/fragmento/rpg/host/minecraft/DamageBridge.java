package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.core.domain.event.DamageApplied;
import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class DamageBridge {

    private final ActorIds actorIds;

    public DamageBridge(ActorIds actorIds, TickBus bus) {
        this.actorIds = Objects.requireNonNull(actorIds);
        bus.subscribe(DamageApplied.class, this::onDamage);
    }

    private void onDamage(DamageApplied e) {
        var uuid = actorIds.uuidOf(e.targetActorId()).orElse(null);
        if (uuid == null) return;

        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (var level : server.getAllLevels()) {
            var ent = level.getEntity(uuid);
            if (ent instanceof LivingEntity le && le.isAlive()) {
                le.hurt(le.damageSources().magic(), (float) e.amount());
                return;
            }
        }
    }
}