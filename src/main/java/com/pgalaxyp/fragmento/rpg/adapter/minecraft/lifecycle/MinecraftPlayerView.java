package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.player.PlayerView;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public record MinecraftPlayerView(long actorId) implements PlayerView {

    @Override
    public Vec3 position() {
        assert ServerLifecycleHooks.getCurrentServer() != null;
        var p = ServerLifecycleHooks.getCurrentServer()
                .getPlayerList()
                .getPlayers()
                .getFirst()
                .position();
        return new Vec3(p.x, p.y, p.z);
    }

    @Override
    public Vec3 lookDirection() {
        assert ServerLifecycleHooks.getCurrentServer() != null;
        var p = ServerLifecycleHooks.getCurrentServer()
                .getPlayerList()
                .getPlayers()
                .getFirst();
        var v = p.getLookAngle();
        return new Vec3(v.x, v.y, v.z);
    }
}