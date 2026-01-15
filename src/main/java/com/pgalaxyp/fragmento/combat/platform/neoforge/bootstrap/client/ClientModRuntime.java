package com.pgalaxyp.fragmento.combat.platform.neoforge.bootstrap.client;

import com.pgalaxyp.fragmento.combat.ports.*;
import com.pgalaxyp.fragmento.combat.ports.dto.*;
import com.pgalaxyp.fragmento.combat.core.ids.*;
import com.pgalaxyp.fragmento.combat.event.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.net.wire.*;
import com.pgalaxyp.fragmento.combat.platform.neoforge.clientfx.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;

public final class ClientModRuntime implements ClientInboundPort {

    public static final ClientModRuntime INSTANCE = new ClientModRuntime();
    private final AtomicReference<GameSnapshot> last = new AtomicReference<>();

    private ClientModRuntime() {}
    public static void install() { NfRuntimeRefs.setClientInbound(INSTANCE); }
    public static void uninstall() { NfRuntimeRefs.clearClientInbound(); }

    @Override
    public void acceptSnapshot(GameSnapshot snapshot) {
        if (snapshot == null) throw new IllegalArgumentException();
        last.set(snapshot);
    }

    @Override
    public void acceptEvents(List<DomainEvent> events) {
        if (events == null) throw new IllegalArgumentException();
        ModVfx.accept(events);
    }

    public static Optional<GameSnapshot> lastSnapshot() { return Optional.ofNullable(INSTANCE.last.get()); }

    public static Optional<ActorId> localActorId() {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer p = mc.player;
        return p == null ? Optional.empty() : Optional.of(new ActorId(p.getUUID()));
    }
}