package com.pgalaxyp.fragmento.rpg.effect.gameplay;

import com.pgalaxyp.fragmento.rpg.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg.content.entity.SpeedZoneEntity;
import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public record SpawnSpeedZoneEffect(
        UUID ownerId,
        double x,
        double y,
        double z,
        int lifeTicks
) implements RpgEffect {

    public UUID spawn(ServerPlayer player) {
        if (player == null) return null;

        var level = player.serverLevel();

        return SpeedZoneEntity.spawn(
                level,
                RpgEntityRegistry.SPEED_ZONE.get(),
                ownerId,
                new Vec3(x, y, z),
                lifeTicks
        );
    }
}