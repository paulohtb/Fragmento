package com.pgalaxyp.fragmento.combat.network.codec.c2s;

import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AttackIntentPayload;
import net.minecraft.network.FriendlyByteBuf;

public final class AttackIntentCodec {

    public static AttackIntentPayload decode(FriendlyByteBuf buf) {
        AttackIntent intent = buf.readEnum(AttackIntent.class);
        return new AttackIntentPayload(intent);
    }

    public static void encode(AttackIntentPayload msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.intent());
    }

    private AttackIntentCodec() {}
}