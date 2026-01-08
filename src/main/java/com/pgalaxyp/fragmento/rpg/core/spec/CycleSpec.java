package com.pgalaxyp.fragmento.rpg.core.spec;

public record CycleSpec(
        ComboCausalitySpec combo,
        TargetingCausalitySpec targeting,
        CommitCausalitySpec commit,
        InterruptCausalitySpec interrupt,
        FrameOrderSpec frameOrder
) {}