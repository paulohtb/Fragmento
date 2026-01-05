package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.adapter.minecraft.context.ActorContextServer;
import com.pgalaxyp.fragmento.rpg.core.loop.TickBus;
import com.pgalaxyp.fragmento.rpg.gameplay.damage.DamageRequest;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.entity.LivingEntity;

public final class DamageBridge {

    private final ActorContextServer context;
    private final ActorIds actorIds;

    public DamageBridge(ActorContextServer context, ActorIds actorIds, TickBus bus) {
        this.context = Objects.requireNonNull(context);
        this.actorIds = Objects.requireNonNull(actorIds);

        bus.subscribe(DamageRequest.class, this::onDamage);
    }

    private void onDamage(DamageRequest r) {
        var uuid = actorIds.uuidOf(r.targetActorId()).orElse(null);
        if (uuid == null) return;

        for (var level : context.player(r.sourceActorId()).map(p -> p.server.getAllLevels()).orElse(List.of())) {
            var e = level.getEntity(uuid);
            if (e instanceof LivingEntity le && le.isAlive()) {
                le.hurt(le.level().damageSources().magic(), (float) r.amount());
                return;
            }
        }
    }
}