package com.pgalaxyp.fragmento.combat.network.payload.c2s;

import com.pgalaxyp.fragmento.combat.domain.id.PlayerId;
import com.pgalaxyp.fragmento.combat.domain.id.SkillId;
import com.pgalaxyp.fragmento.combat.domain.input.WeaponInputType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SkillIntentPayload(
        PlayerId playerId,
        SkillId skillId,
        WeaponInputType inputType,
        long targetEntityId
) implements CustomPacketPayload {

    public static final Type<SkillIntentPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("fragmento", "skill_intent"));

    public SkillIntentPayload {
        if (playerId == null) {
            throw new IllegalArgumentException("SkillIntent sem playerId");
        }
        if (skillId == null) {
            throw new IllegalArgumentException("SkillIntent sem skillId");
        }
        if (inputType != WeaponInputType.SKILL_PRESS && inputType != WeaponInputType.SKILL_CANCEL) {
            throw new IllegalArgumentException("SkillIntent inputType invalido");
        }
        targetEntityId = Math.max(0L, targetEntityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}