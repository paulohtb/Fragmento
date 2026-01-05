package com.pgalaxyp.fragmento.rpg.network.payload.c2s;

import com.pgalaxyp.fragmento.rpg.gameplay.input.InputAction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record InputIntentPayload(long actorId, InputAction action) implements CustomPacketPayload {

    public static final Type<InputIntentPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    "fragmento",
                    "input_intent"));

    public static final StreamCodec<FriendlyByteBuf, InputIntentPayload> STREAM_CODEC =
            StreamCodec.of(
                    (buf, p) -> {
                        buf.writeLong(p.actorId());
                        buf.writeEnum(p.action());
                    },
                    buf -> new InputIntentPayload(
                            buf.readLong(),
                            buf.readEnum(InputAction.class)
                    )
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}