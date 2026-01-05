package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.TargetRaycastService;
import com.pgalaxyp.fragmento.rpg.gameplay.weapon.WeaponRepository;
import java.util.Objects;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class PlayerSyncBridge {

    private final ActorIds actorIds;
    private final ActorRepository actors;
    private final WeaponRepository weapons;

    public PlayerSyncBridge(ActorIds actorIds, ActorRepository actors, WeaponRepository weapons) {
        this.actorIds = Objects.requireNonNull(actorIds);
        this.actors = Objects.requireNonNull(actors);
        this.weapons = Objects.requireNonNull(weapons);
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent.Post event) {
        var server = event.getServer();
        var sp = server.getPlayerList().getPlayers().stream().findFirst().orElse(null);
        if (sp == null) return;

        var actorId = actorIds.idFor(sp.getUUID());

        weapons.equip(actorId, "FLUTE");

        var pos = sp.position();
        var p = new Vec3(pos.x, pos.y, pos.z);

        actors.setPositionAndBounds(actorId, p, TargetRaycastService.defaultBoundsAt(p));
    }
}