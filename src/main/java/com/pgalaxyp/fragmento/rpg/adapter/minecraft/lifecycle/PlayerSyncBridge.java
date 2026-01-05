package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.actor.ActorRepository;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.gameplay.targeting.TargetRaycastService;
import com.pgalaxyp.fragmento.rpg.platform.api.player.PlayerView;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public final class PlayerSyncBridge {

    private final PlayerView player;
    private final ActorRepository actors;

    public PlayerSyncBridge(PlayerView player, ActorRepository actors) {
        this.player = player;
        this.actors = actors;
    }

    @SubscribeEvent
    public void onTick(ServerTickEvent.Post event) {
        var server = event.getServer();
        var sp = server.getPlayerList().getPlayers().stream().findFirst().orElse(null);
        if (sp == null) return;

        var pos = sp.position();
        Vec3 pos1 = new Vec3(pos.x, pos.y, pos.z);
        actors.setPositionAndBounds(
                player.actorId(),
                pos1,
                TargetRaycastService.defaultBoundsAt(pos1)
        );
    }
}