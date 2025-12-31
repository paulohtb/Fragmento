package com.pgalaxyp.fragmento.combat.network.codec;

import com.pgalaxyp.fragmento.combat.domain.id.CatalystId;
import com.pgalaxyp.fragmento.combat.domain.id.PlayerId;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.WeaponInputType;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AttackIntentPayload;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.SkillIntentPayload;
import net.minecraft.network.FriendlyByteBuf;

public final class IntentCodec {

    public static AttackIntentPayload decodeAttackIntent(FriendlyByteBuf buf) {
        PlayerId playerId = new PlayerId(buf.readUUID());
        CatalystId catalystId = new CatalystId(buf.readVarInt());
        return new AttackIntentPayload(playerId, catalystId);
    }

    public static void encodeAttackIntent(AttackIntentPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId().value());
        buf.writeVarInt(msg.catalystId().value());
    }

    public static SkillIntentPayload decodeSkillIntent(FriendlyByteBuf buf) {
        PlayerId playerId = new PlayerId(buf.readUUID());
        SkillId skillId = new SkillId(buf.readVarInt());

        int ord = buf.readVarInt();
        WeaponInputType[] values = WeaponInputType.values();
        int safeOrd = Math.max(0, Math.min(ord, values.length - 1));
        WeaponInputType resolved = values[safeOrd];

        if (resolved != WeaponInputType.SKILL_PRESS && resolved != WeaponInputType.SKILL_CANCEL) {
            resolved = WeaponInputType.SKILL_PRESS;
        }

        long targetEntityId = buf.readVarLong();
        return new SkillIntentPayload(playerId, skillId, resolved, targetEntityId);
    }

    public static void encodeSkillIntent(SkillIntentPayload msg, FriendlyByteBuf buf) {
        buf.writeUUID(msg.playerId().value());
        buf.writeVarInt(msg.skillId().value());
        buf.writeVarInt(msg.inputType().ordinal());
        buf.writeVarLong(Math.max(0L, msg.targetEntityId()));
    }

    private IntentCodec() {}
}