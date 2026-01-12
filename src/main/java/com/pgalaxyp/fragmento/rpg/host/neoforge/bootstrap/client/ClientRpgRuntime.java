package com.pgalaxyp.fragmento.rpg.host.neoforge.bootstrap.client;

import com.pgalaxyp.fragmento.rpg.core.domain.ids.ActorId;
import com.pgalaxyp.fragmento.rpg.core.events.event.DomainEvent;
import com.pgalaxyp.fragmento.rpg.host.neoforge.clientfx.ModVfx;
import com.pgalaxyp.fragmento.rpg.platform.neoforge.net.wire.NeoForgeNetRuntimeRefs;
import com.pgalaxyp.fragmento.rpg.ports.ClientInboundPort;
import com.pgalaxyp.fragmento.rpg.ports.dto.GameSnapshot;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class ClientRpgRuntime implements ClientInboundPort {

    public static final ClientRpgRuntime INSTANCE = new ClientRpgRuntime();

    private final AtomicReference<GameSnapshot> last = new AtomicReference<>();

    private ClientRpgRuntime() {}

    public static void install() {
        NeoForgeNetRuntimeRefs.setClientInbound(INSTANCE);
    }

    public static void uninstall() {
        NeoForgeNetRuntimeRefs.clearClientInbound();
    }

    @Override
    public void acceptSnapshot(GameSnapshot snapshot) {
        if (snapshot == null) {
            throw new IllegalArgumentException();
        }
        last.set(snapshot);
    }

    @Override
    public void acceptEvents(List<DomainEvent> events) {
        if (events == null) {
            throw new IllegalArgumentException();
        }
        ModVfx.accept(events);
    }

    public static Optional<GameSnapshot> lastSnapshot() {
        return Optional.ofNullable(INSTANCE.last.get());
    }

    public static Optional<ActorId> localActorId() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        if (p == null) {
            return Optional.empty();
        }
        return Optional.of(new ActorId(p.getUUID()));
    }
}