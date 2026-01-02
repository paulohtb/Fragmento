package com.pgalaxyp.fragmento.combat.network.payload.c2s;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record AbilityIntentPayload(
        AbilityIntent intent
) implements CustomPacketPayload {

    public static final Type<AbilityIntentPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    "fragmento",
                    "ability_intent"
            ));

    private static final StreamCodec<ByteBuf, SkillSlotId> SLOT_CODEC =
            ByteBufCodecs.VAR_INT.map(SkillSlotId::new, SkillSlotId::index);

    private static final StreamCodec<ByteBuf, AbilityIntentKind> KIND_CODEC =
            ByteBufCodecs.VAR_INT.map(
                    i -> AbilityIntentKind.values()[i],
                    AbilityIntentKind::ordinal
            );

    public static final StreamCodec<ByteBuf, AbilityIntentPayload> STREAM_CODEC =
            StreamCodec.composite(
                    SLOT_CODEC,
                    p -> p.intent().slot(),
                    KIND_CODEC,
                    p -> p.intent().kind(),
                    (slot, kind) -> new AbilityIntentPayload(new AbilityIntent(slot, kind))
            );

    public AbilityIntentPayload {
        if (intent == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}