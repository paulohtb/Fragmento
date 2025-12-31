package com.pgalaxyp.fragmento.combat.network.codec;

import com.pgalaxyp.fragmento.combat.domain.cue.AnimationCue;
import com.pgalaxyp.fragmento.combat.domain.cue.AnimationKey;
import com.pgalaxyp.fragmento.combat.domain.id.PlayerId;
import com.pgalaxyp.fragmento.combat.network.payload.s2c.VisualCuePayload;
import net.minecraft.network.FriendlyByteBuf;

public final class CueCodec {

    public static VisualCuePayload decode(FriendlyByteBuf buf) {
        PlayerId playerId = new PlayerId(buf.readUUID());
        boolean hasKey = buf.readBoolean();
        AnimationKey key = hasKey ? new AnimationKey(buf.readUtf()) : null;
        long startedAt = buf.readVarLong();
        return new VisualCuePayload(playerId, new AnimationCue(key, startedAt));
    }

    public static void encode(VisualCuePayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId().value());
        AnimationCue cue = msg.cue();
        AnimationKey key = cue.key();
        buf.writeBoolean(key != null);
        if (key != null) {
            buf.writeUtf(key.id());
        }
        buf.writeVarLong(cue.startedAtTick());
    }

    private CueCodec() {}
}