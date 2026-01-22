package com.pgalaxyp.fragmento.combat.client;

import com.pgalaxyp.fragmento.combat.ability.api.AbilitySnapshot;
import com.pgalaxyp.fragmento.combat.actor.ActorId;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;

public final class AbilityFeedbackRenderer {
    private final ClientSnapshotReceiver snapshots;

    public AbilityFeedbackRenderer(ClientSnapshotReceiver snapshots) {
        this.snapshots = snapshots;
    }

    public void renderTick() {
        var snap = snapshots.lastSnapshot();
        if (snap == null) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        ActorId self = new ActorId(player.getUUID());
        long frame = snap.frame().frameId();

        for (AbilitySnapshot a : snap.abilities().active()) {
            if (!a.actorId().equals(self)) continue;
            if (!a.activeAt(frame)) continue;

            player.level().addParticle(
                    ParticleTypes.NOTE,
                    player.getX(),
                    player.getEyeY(),
                    player.getZ(),
                    0.0,
                    0.5,
                    0.0
            );
        }
    }
}