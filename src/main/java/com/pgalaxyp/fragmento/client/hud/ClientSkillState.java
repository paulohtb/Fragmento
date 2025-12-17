package com.pgalaxyp.fragmento.client.hud;

import com.pgalaxyp.fragmento.system.skill.SkillSlot;

import java.util.EnumMap;
import java.util.UUID;

public final class ClientSkillState {

    private static final EnumMap<SkillSlot, Integer> COOLDOWNS =
            new EnumMap<>(SkillSlot.class);

    private static UUID instrumentId;
    private static int charge;
    private static int maxCharge;

    private ClientSkillState() {
    }

    public static void update(
            EnumMap<SkillSlot, Integer> cooldowns,
            UUID inst,
            int c,
            int max
    ) {
        COOLDOWNS.clear();
        COOLDOWNS.putAll(cooldowns);
        instrumentId = inst;
        charge = c;
        maxCharge = max;
    }

    public static int cooldown(SkillSlot slot) {
        return COOLDOWNS.getOrDefault(slot, 0);
    }

    public static int charge() {
        return charge;
    }

    public static int maxCharge() {
        return maxCharge;
    }

    public static UUID instrumentId() {
        return instrumentId;
    }
}