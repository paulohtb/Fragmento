package com.pgalaxyp.fragmento.combat.targetingModule.api;

import com.pgalaxyp.fragmento.combat.actorModule.api.ActorId;
import java.util.Optional;

public record TargetResult(Target target, TargetingFallback appliedFallback) {
    public TargetResult { if (target == null) throw new IllegalArgumentException(); }
    public Optional<TargetingFallback> appliedFallbackOpt() { return Optional.ofNullable(appliedFallback); }
    public boolean usedFallback() { return appliedFallback != null; }
    public Optional<ActorId> actorTargetOpt() { return target instanceof ActorTarget(ActorId actorId) ? Optional.of(actorId) : Optional.empty(); }
    public ActorId actorTargetIdOrNull() { return target instanceof ActorTarget(ActorId actorId) ? actorId : null; }

    public static TargetResult direct(Target target) {
        if (target == null) throw new IllegalArgumentException();
        return new TargetResult(target, null);
    }

    public static TargetResult fallback(Target target, TargetingFallback policy) {
        if (target == null || policy == null) throw new IllegalArgumentException();
        return new TargetResult(target, policy);
    }
}