package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.intentModule.api.IntentSinkPort;
import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import com.pgalaxyp.fragmento.combat.contentModule.minecraft.FragmentoMinecraftContent;
import java.util.concurrent.atomic.AtomicReference;
import net.neoforged.fml.common.Mod;
import net.neoforged.bus.api.IEventBus;

@Mod(FragmentoPlatform.MODID)
public final class FragmentoMod {
    public static final AtomicReference<IntentSinkPort> INTEGRATED_SERVER_INTENTS = new AtomicReference<>();
    public static final ClientSnapshotReceiver CLIENT_RECEIVER = new ClientSnapshotReceiver();

    public FragmentoMod(IEventBus modBus) {
        FragmentoMinecraftContent.REGISTRY.register(modBus);
    }
}