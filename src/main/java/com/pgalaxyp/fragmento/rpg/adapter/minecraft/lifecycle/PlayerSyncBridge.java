package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.gameplay.state.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.state.WeaponRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.TargetRaycastService;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class PlayerSyncBridge {

    private final ActorIds actorIds;
    private final ActorRepository actors;
    private final WeaponRepository weapons;

    public PlayerSyncBridge(ActorIds actorIds, ActorRepository actors, WeaponRepository weapons) {
        this.actorIds = actorIds;
        this.actors = actors;
        this.weapons = weapons;
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent.Post event) {
        for (var sp : event.getServer().getPlayerList().getPlayers()) {
            var actorId = actorIds.idFor(sp.getUUID());

            if (weapons.equippedWeapon(actorId).isEmpty()) {
                weapons.equip(actorId, "FLUTE");
            }

            var pos = sp.position();
            var look = sp.getLookAngle();

            var p = new Vec3(pos.x, pos.y, pos.z);
            var d = new Vec3(look.x, look.y, look.z);

            actors.setState(actorId, p, d, TargetRaycastService.defaultBoundsAt(p));
        }
    }
}