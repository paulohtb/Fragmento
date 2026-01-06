package com.pgalaxyp.fragmento.rpg.core.domain.targeting;

import java.util.Set;

public record TargetingRequest(
        TargetRelation relation,
        TargetPolicy policy,
        Set<TargetFilter> filters
) {}