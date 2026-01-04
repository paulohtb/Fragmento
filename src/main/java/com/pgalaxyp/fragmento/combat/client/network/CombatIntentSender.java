package com.pgalaxyp.fragmento.combat.client.network;

import com.pgalaxyp.fragmento.bootstrap.logging.FragmentoLog;
import com.pgalaxyp.fragmento.combat.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.combat.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.combat.network.payload.c2s.AttackIntentPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CombatIntentSender {

    public void sendAttack(AttackIntent intent) {
        if (intent == null) {
            return;
        }
        FragmentoLog.intent("C2S attack intent={}", intent);
        PacketDistributor.sendToServer(new AttackIntentPayload(intent));
    }

    public void sendAbility(AbilityIntent intent) {
        if (intent == null) {
            return;
        }
        FragmentoLog.intent(
                "C2S ability kind={} slot={}",
                intent.kind(),
                intent.slot().index()
        );
        PacketDistributor.sendToServer(new AbilityIntentPayload(intent));
    }
}