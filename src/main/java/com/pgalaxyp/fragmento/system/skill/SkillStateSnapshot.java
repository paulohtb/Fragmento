package com.pgalaxyp.fragmento.system.skill;

import java.util.EnumMap;
import java.util.UUID;

public final class SkillStateSnapshot {

    private final UUID playerId;
    private final EnumMap<SkillSlot, Integer> cooldowns;
    private final UUID instrumentId;
    private final int charge;
    private final int maxCharge;

    public SkillStateSnapshot(
            UUID playerId,
            EnumMap<SkillSlot, Integer> cooldowns,
            UUID instrumentId,
            int charge,
            int maxCharge
    ) {
        this.playerId = playerId;
        this.cooldowns = cooldowns;
        this.instrumentId = instrumentId;
        this.charge = charge;
        this.maxCharge = maxCharge;
    }

    public UUID playerId() {
        return playerId;
    }

    public EnumMap<SkillSlot, Integer> cooldowns() {
        return cooldowns;
    }

    public UUID instrumentId() {
        return instrumentId;
    }

    public int charge() {
        return charge;
    }

    public int maxCharge() {
        return maxCharge;
    }
}