package com.pgalaxyp.fragmento.combat.network.codec.c2s;

import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.combat.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AbilityIntentPayload;
import net.minecraft.network.FriendlyByteBuf;

public final class AbilityIntentCodec {

    public static AbilityIntentPayload decode(FriendlyByteBuf buf) {
        SkillSlotId slot = new SkillSlotId(buf.readVarInt());
        AbilityIntentKind kind = buf.readEnum(AbilityIntentKind.class);
        return new AbilityIntentPayload(new AbilityIntent(slot, kind));
    }

    public static void encode(AbilityIntentPayload msg, FriendlyByteBuf buf) {
        AbilityIntent intent = msg.intent();
        buf.writeVarInt(intent.slot().index());
        buf.writeEnum(intent.kind());
    }

    private AbilityIntentCodec() {}
}