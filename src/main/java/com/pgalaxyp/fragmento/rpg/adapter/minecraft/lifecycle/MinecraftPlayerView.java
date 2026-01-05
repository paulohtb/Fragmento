package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.adapter.minecraft.context.ActorContextServer;
import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.player.PlayerView;
import java.util.Objects;

public final class MinecraftPlayerView implements PlayerView {

    private final long actorId;
    private final ActorContextServer context;

    public MinecraftPlayerView(long actorId, ActorContextServer context) {
        this.actorId = actorId;
        this.context = Objects.requireNonNull(context);
    }

    @Override
    public long actorId() {
        return actorId;
    }

    @Override
    public Vec3 position() {
        var p = context.player(actorId).orElse(null);
        if (p == null) return new Vec3(0, 0, 0);

        var v = p.position();
        return new Vec3(v.x, v.y, v.z);
    }

    @Override
    public Vec3 lookDirection() {
        var p = context.player(actorId).orElse(null);
        if (p == null) return new Vec3(0, 0, 0);

        var v = p.getLookAngle();
        return new Vec3(v.x, v.y, v.z);
    }
}