package com.pgalaxyp.fragmento.rpg_old.effect.gameplay;

import com.pgalaxyp.fragmento.rpg_old.content.entity.InfusedStrikeEntity;
import com.pgalaxyp.fragmento.rpg_old.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg_old.effect.RpgEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public record SpawnInfusedStrikeEffect(
        UUID ownerId,
        float damage,
        double startX,
        double startY,
        double startZ,
        double impactX,
        double impactY,
        double impactZ
) implements RpgEffect {

    public UUID spawn(ServerPlayer player) {
        if (player == null) return null;

        var level = player.serverLevel();

        return InfusedStrikeEntity.spawn(
                level,
                RpgEntityRegistry.INFUSED_STRIKE.get(),
                ownerId,
                damage,
                new Vec3(startX, startY, startZ),
                new Vec3(impactX, impactY, impactZ)
        );
    }
}