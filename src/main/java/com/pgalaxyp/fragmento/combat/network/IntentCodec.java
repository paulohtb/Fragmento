package com.pgalaxyp.fragmento.combat.network;

import net.minecraft.network.FriendlyByteBuf;

public final class IntentCodec {

    public static AttackIntentPayload decodeAttackIntent(FriendlyByteBuf buf) {
        var playerId = buf.readUUID();
        int catalyst = buf.readVarInt();
        return new AttackIntentPayload(playerId, catalyst);
    }

    public static void encodeAttackIntent(AttackIntentPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId());
        buf.writeVarInt(msg.catalystId());
    }

    private IntentCodec() {}
}