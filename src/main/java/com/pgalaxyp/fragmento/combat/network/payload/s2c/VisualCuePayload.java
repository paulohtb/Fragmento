package com.pgalaxyp.fragmento.combat.network.payload.s2c;

import com.pgalaxyp.fragmento.combat.domain.cue.AnimationCue;
import com.pgalaxyp.fragmento.combat.domain.id.PlayerId;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record VisualCuePayload(
        PlayerId playerId,
        AnimationCue cue
) implements CustomPacketPayload {

    public static final Type<VisualCuePayload> TYPE =
            new Type<>(ResourceLocation
                    .fromNamespaceAndPath("fragmento", "visual_cue"));

    public VisualCuePayload {
        if (playerId == null) {
            throw new IllegalArgumentException("VisualCuePayload sem playerId");
        }
        if (cue == null) {
            throw new IllegalArgumentException("VisualCuePayload sem cue");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}