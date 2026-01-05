package com.pgalaxyp.fragmento.rpg.adapter.minecraft.lifecycle;

import com.pgalaxyp.fragmento.rpg.gameplay.math.Vec3;
import com.pgalaxyp.fragmento.rpg.platform.api.world.EntityHit;
import com.pgalaxyp.fragmento.rpg.platform.api.world.RaycastQuery;
import com.pgalaxyp.fragmento.rpg.platform.api.world.WorldView;

import java.util.Optional;

public final class MinecraftWorldView implements WorldView {

    @Override
    public Optional<EntityHit> raycastLivingEntity(RaycastQuery query) {
        return Optional.empty();
    }

    @Override
    public boolean isSolidAt(Vec3 position) {
        return false;
    }
}