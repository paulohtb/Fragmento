package com.pgalaxyp.fragmento.rpg.platform.minecraft.runtime;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.event.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.engine.snapshot.GameSnapshot;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.ids.MinecraftActorIds;
import com.pgalaxyp.fragmento.rpg.platform.minecraft.net.FragmentoNetBytes;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.pgalaxyp.fragmento.rpg.platform.minecraft.vfx.FragmentoVfx;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class FragmentoClientRuntime {

    private static final AtomicReference<GameSnapshot> last = new AtomicReference<>();

    public static void acceptSnapshotBytes(byte[] data) {
        GameSnapshot s = FragmentoNetBytes.decodeSnapshot(data);
        last.set(s);
    }

    public static void acceptEvents(List<?> events) {
        if (events == null) {
            throw new IllegalArgumentException();
        }
        List<DomainEvent> out = (List<DomainEvent>) events;
        FragmentoVfx.accept(out);
    }

    public static Optional<GameSnapshot> lastSnapshot() {
        return Optional.ofNullable(last.get());
    }

    public static Optional<ActorId> localActorId() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) {
            return Optional.empty();
        }
        return Optional.of(MinecraftActorIds.fromUuid(p.getUUID()));
    }

    private FragmentoClientRuntime() {}
}