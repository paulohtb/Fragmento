package com.pgalaxyp.fragmento.combat.targeting.api;

import com.pgalaxyp.fragmento.combat.actor.ActorId;

import java.util.*;

public record TargetResult(Target target, TargetingFallback appliedFallback, ActorId actorTargetId) {

    public TargetResult {
        if (target == null) {
            throw new IllegalArgumentException();
        }
    }

    public Optional<TargetingFallback> appliedFallbackOpt() {
        return Optional.ofNullable(appliedFallback);
    }

    public Optional<ActorId> actorTargetOpt() {
        return Optional.ofNullable(actorTargetId);
    }

    public boolean usedFallback() {
        return appliedFallback != null;
    }

    public static TargetResult direct(Target target, ActorId actorTargetId) {
        if (target == null) {
            throw new IllegalArgumentException();
        }

        return new TargetResult(target, null, actorTargetId);
    }

    public static TargetResult fallback(Target target, TargetingFallback policy, ActorId actorTargetId) {
        if (target == null || policy == null) {
            throw new IllegalArgumentException();
        }

        return new TargetResult(target, policy, actorTargetId);
    }
}