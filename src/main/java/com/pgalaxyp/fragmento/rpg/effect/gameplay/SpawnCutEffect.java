package com.pgalaxyp.fragmento.rpg.effect.gameplay;

import com.pgalaxyp.fragmento.rpg.content.entity.CutEffectEntity;
import com.pgalaxyp.fragmento.rpg.content.entity.CutOrientation;
import com.pgalaxyp.fragmento.rpg.content.entity.RpgEntityRegistry;
import com.pgalaxyp.fragmento.rpg.effect.RpgEffect;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import java.util.UUID;

public record SpawnCutEffect(
        UUID ownerId,
        UUID targetId,
        double spawnX,
        double spawnY,
        double spawnZ,
        double targetX,
        double targetY,
        double targetZ,
        int lifeTicks,
        float damage,
        CutOrientation orientation
) implements RpgEffect {

    public UUID spawn(ServerPlayer player) {
        if (player == null) return null;

        var level = player.serverLevel();

        return CutEffectEntity.spawn(
                level,
                RpgEntityRegistry.CUT.get(),
                ownerId,
                targetId,
                new Vec3(spawnX, spawnY, spawnZ),
                new Vec3(targetX, targetY, targetZ),
                lifeTicks,
                damage,
                orientation
        );
    }
}