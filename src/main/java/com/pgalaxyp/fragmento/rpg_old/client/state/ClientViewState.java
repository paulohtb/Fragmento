package com.pgalaxyp.fragmento.rpg_old.client.state;

import com.pgalaxyp.fragmento.rpg_old.domain.timing.Time;
import com.pgalaxyp.fragmento.rpg_old.state.snapshot.CombatSnapshot;
import net.minecraft.client.Minecraft;

public final class ClientViewState {

    private CombatSnapshot last;
    private long receivedAtClientTick;

    public void apply(CombatSnapshot snapshot) {
        if (snapshot == null || snapshot.version() == null) return;

        if (last == null || snapshot.version().isAfter(last.version())) {
            last = snapshot;

            Minecraft mc = Minecraft.getInstance();
            if (mc != null && mc.level != null) {
                receivedAtClientTick = mc.level.getGameTime();
            } else {
                receivedAtClientTick = 0L;
            }
        }
    }

    public CombatSnapshot current() {
        return last;
    }

    public Time now() {
        CombatSnapshot snap = last;
        if (snap == null || snap.now() == null) return Time.ofTicks(0L);

        Minecraft mc = Minecraft.getInstance();
        long clientTick = 0L;
        if (mc != null && mc.level != null) {
            clientTick = mc.level.getGameTime();
        }

        long delta = Math.max(0L, clientTick - receivedAtClientTick);
        return Time.ofTicks(snap.now().ticks() + delta);
    }
}