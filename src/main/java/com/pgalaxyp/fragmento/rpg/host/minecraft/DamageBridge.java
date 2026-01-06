package com.pgalaxyp.fragmento.rpg.host.minecraft;

import com.pgalaxyp.fragmento.rpg.engine.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.core.domain.damage.DamageRequest;
import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public final class DamageBridge {

    private final ActorIds actorIds;

    public DamageBridge(ActorIds actorIds, TickBus bus) {
        this.actorIds = Objects.requireNonNull(actorIds);
        bus.subscribe(DamageRequest.class, this::onDamage);
    }

    private void onDamage(DamageRequest r) {
        var uuid = actorIds.uuidOf(r.targetActorId()).orElse(null);
        if (uuid == null) return;

        var server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) return;

        for (var level : server.getAllLevels()) {
            var e = level.getEntity(uuid);
            if (e instanceof LivingEntity le && le.isAlive()) {
                le.hurt(le.damageSources().magic(), (float) r.amount());
                return;
            }
        }
    }
}