package com.pgalaxyp.fragmento.rpg.client;

import com.pgalaxyp.fragmento.rpg.client.network.ClientIntentSender;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.AbilityIntentKind;
import com.pgalaxyp.fragmento.rpg.domain.input.AttackIntent;
import com.pgalaxyp.fragmento.rpg.domain.input.SkillSlotId;
import com.pgalaxyp.fragmento.rpg.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg.state.snapshot.CombatSnapshot;

public final class ClientNetworkProxy {

    private static final ClientTime TIME = new ClientTime();
    private static final ClientIntentSender INTENT_SENDER = new ClientIntentSender();

    private static CombatSnapshot snapshot;

    public static void applySnapshot(CombatSnapshot snap) {
        snapshot = snap;
        if (snap != null) {
            TIME.sync(snap.now());
        }
    }

    public static CombatSnapshot snapshot() {
        return snapshot;
    }

    public static Time now() {
        return TIME.now();
    }

    public static void sendSkillPress(SkillSlotId slot) {
        if (slot == null) return;
        INTENT_SENDER.sendAbility(new AbilityIntent(slot, AbilityIntentKind.PRESS));
    }

    public static void sendSkillRelease(SkillSlotId slot) {
        if (slot == null) return;
        INTENT_SENDER.sendAbility(new AbilityIntent(slot, AbilityIntentKind.CANCEL));
    }

    public static void sendAttackIntent(AttackIntent intent) {
        if (intent == null) return;
        INTENT_SENDER.sendAttack(intent);
    }

    private ClientNetworkProxy() {}
}