package com.pgalaxyp.fragmento.rpg.core.spec;

public record GameSpec(
        FrameSpec frame,
        ComboSpec combo,
        TargetingSpec targeting
) {}