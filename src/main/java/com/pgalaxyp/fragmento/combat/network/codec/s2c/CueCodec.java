package com.pgalaxyp.fragmento.combat.network.codec.s2c;

import com.pgalaxyp.fragmento.combat.domain.cue.AnimationCue;
import com.pgalaxyp.fragmento.combat.domain.cue.AnimationKey;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.VisualCuePayload;
import net.minecraft.network.FriendlyByteBuf;

public final class CueCodec {

    public static VisualCuePayload decode(FriendlyByteBuf buf) {
        AnimationKey key = new AnimationKey(buf.readUtf());
        long startedAt = buf.readVarLong();
        return new VisualCuePayload(new AnimationCue(key, startedAt));
    }

    public static void encode(VisualCuePayload msg, FriendlyByteBuf buf) {
        AnimationCue cue = msg.cue();
        buf.writeUtf(cue.key().id());
        buf.writeVarLong(cue.startedAtTick());
    }

    private CueCodec() {}
}