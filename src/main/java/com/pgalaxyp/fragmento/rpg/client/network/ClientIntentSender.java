package com.pgalaxyp.fragmento.rpg.client.network;

import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.AbilityIntentPayload;
import com.pgalaxyp.fragmento.rpg.network.payload.c2s.AttackIntentPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public final class ClientIntentSender {

    public void sendAttack(AttackIntent intent) {
        if (intent == null) return;
        PacketDistributor.sendToServer(new AttackIntentPayload(intent));
    }

    public void sendAbility(AbilityIntent intent) {
        if (intent == null) return;
        PacketDistributor.sendToServer(new AbilityIntentPayload(intent));
    }
}