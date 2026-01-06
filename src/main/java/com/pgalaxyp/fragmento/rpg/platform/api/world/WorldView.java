package com.pgalaxyp.fragmento.rpg.platform.api.world;

import java.util.Optional;

public interface WorldView {

    Optional<EntityHit> raycastLivingEntity(TargetingQuery query);

    WorldVectors casterVectors(long actorId);

    record WorldVectors(Vec3d pos, Vec3d look) {}

    record Vec3d(double x, double y, double z) {}
}