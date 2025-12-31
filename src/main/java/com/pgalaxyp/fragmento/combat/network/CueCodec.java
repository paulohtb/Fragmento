package com.pgalaxyp.fragmento.combat.network;

import com.pgalaxyp.fragmento.combat.domain.animation.AnimationCue;
import com.pgalaxyp.fragmento.combat.domain.animation.AnimationKey;
import net.minecraft.network.FriendlyByteBuf;
import java.util.UUID;

public final class CueCodec {

    public static VisualCuePayload decode(FriendlyByteBuf buf) {
        UUID player = buf.readUUID();
        boolean hasKey = buf.readBoolean();
        AnimationKey key = hasKey ? new AnimationKey(buf.readUtf()) : null;
        long startedAt = buf.readVarLong();
        return new VisualCuePayload(player, new AnimationCue(key, startedAt));
    }

    public static void encode(VisualCuePayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId());
        AnimationCue cue = msg.cue();
        if (cue.key() != null) {
            buf.writeBoolean(true);
            buf.writeUtf(cue.key().id());
        } else {
            buf.writeBoolean(false);
        }
        buf.writeVarLong(cue.startedAtTick());
    }

    private CueCodec() {}
}