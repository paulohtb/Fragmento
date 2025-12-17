package com.pgalaxyp.fragmento.system.skill;

import com.pgalaxyp.fragmento.content.bard.constants.BardInstrumentConstants;
import com.pgalaxyp.fragmento.network.s2c.SkillStateSnapshotPacket;
import com.pgalaxyp.fragmento.system.charge.ChargeInstance;
import com.pgalaxyp.fragmento.system.charge.ChargeSystem;
import com.pgalaxyp.fragmento.system.gameplay.cooldown.PlayerSkillCooldownSavedData;
import net.minecraft.server.level.ServerPlayer;

import java.util.EnumMap;
import java.util.UUID;

public final class SkillStateSnapshotService {

    private final ChargeSystem charges;

    public SkillStateSnapshotService(ChargeSystem charges) {
        this.charges = charges;
    }

    public SkillStateSnapshot build(ServerPlayer player, UUID instrumentId) {
        EnumMap<SkillSlot, Integer> cds = new EnumMap<>(SkillSlot.class);

        long now = player.level().getGameTime();
        var saved = PlayerSkillCooldownSavedData.get(player.server);

        for (SkillSlot slot : SkillSlot.values()) {
            cds.put(slot, saved.remainingTicks(player.getUUID(), slot, now));
        }

        ChargeInstance c = charges.getOrCreate(instrumentId, BardInstrumentConstants.MAX_CHARGE);

        return new SkillStateSnapshot(
                player.getUUID(),
                cds,
                instrumentId,
                c.value(),
                BardInstrumentConstants.MAX_CHARGE
        );
    }

    public SkillStateSnapshotPacket toPacket(SkillStateSnapshot snapshot) {
        return new SkillStateSnapshotPacket(
                snapshot.playerId(),
                snapshot.cooldowns(),
                snapshot.instrumentId(),
                snapshot.charge(),
                snapshot.maxCharge()
        );
    }
}