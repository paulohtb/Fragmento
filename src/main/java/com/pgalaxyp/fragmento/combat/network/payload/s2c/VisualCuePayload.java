package com.pgalaxyp.fragmento.combat.network.payload.s2c;

import com.pgalaxyp.fragmento.combat.domain.cue.AnimationCue;
import com.pgalaxyp.fragmento.combat.domain.cue.AnimationKey;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record VisualCuePayload(
        AnimationCue cue
) implements CustomPacketPayload {

    public static final Type<VisualCuePayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "visual_cue"));

    public static final StreamCodec<ByteBuf, VisualCuePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8.map(AnimationKey::new, AnimationKey::id),
                    p -> p.cue().key(),
                    ByteBufCodecs.VAR_LONG,
                    p -> p.cue().startedAtTick(),
                    (key, startedAt) -> new VisualCuePayload(new AnimationCue(key, startedAt))
            );

    public VisualCuePayload {
        if (cue == null) {
            throw new IllegalArgumentException("VisualCuePayload sem cue");
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}