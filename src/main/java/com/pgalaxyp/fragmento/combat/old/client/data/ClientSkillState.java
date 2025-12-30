package com.pgalaxyp.fragmento.combat.old.client.data;

import com.pgalaxyp.fragmento.combat.old.system.skill.SkillSlot;

import java.util.EnumMap;
import java.util.UUID;

public final class ClientSkillState {

    private static final EnumMap<SkillSlot, Integer> COOLDOWNS = new EnumMap<>(SkillSlot.class);

    private static UUID instrumentId;
    private static int charge;
    private static int maxCharge;

    private ClientSkillState() {
    }

    public static void clear() {
        COOLDOWNS.clear();
        instrumentId = null;
        charge = 0;
        maxCharge = 0;
    }

    public static void update(
            EnumMap<SkillSlot, Integer> cooldowns,
            UUID instrumentIdValue,
            int chargeValue,
            int maxChargeValue
    ) {
        COOLDOWNS.clear();
        if (cooldowns != null) {
            COOLDOWNS.putAll(cooldowns);
        }

        instrumentId = instrumentIdValue;
        charge = Math.max(0, chargeValue);
        maxCharge = Math.max(0, maxChargeValue);
    }

    public static int cooldown(SkillSlot slot) {
        if (slot == null) return 0;
        return COOLDOWNS.getOrDefault(slot, 0);
    }

    public static UUID instrumentId() {
        return instrumentId;
    }

    public static int charge() {
        return charge;
    }

    public static int maxCharge() {
        return maxCharge;
    }
}