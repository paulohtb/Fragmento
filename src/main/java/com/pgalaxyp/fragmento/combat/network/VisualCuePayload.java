package com.pgalaxyp.fragmento.combat.network;

import com.pgalaxyp.fragmento.combat.domain.animation.AnimationCue;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record VisualCuePayload(UUID playerId, AnimationCue cue)
        implements CustomPacketPayload {

    public static final Type<VisualCuePayload> TYPE =
            new Type<>(ResourceLocation
                    .fromNamespaceAndPath("fragmento", "visual_cue"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}