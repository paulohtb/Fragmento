package com.pgalaxyp.fragmento.combat.mod;

import com.pgalaxyp.fragmento.combat.actorModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.api.*;
import com.pgalaxyp.fragmento.combat.inputModule.minecraft.*;
import com.pgalaxyp.fragmento.combat.intentModule.api.IntentSinkPort;
import com.pgalaxyp.fragmento.combat.platformModule.FragmentoPlatform;
import com.pgalaxyp.fragmento.combat.inputModule.port.ActorInputContextProvider;
import com.pgalaxyp.fragmento.combat.inputModule.system.PrimaryActionInputHandler;
import com.pgalaxyp.fragmento.combat.contentModule.minecraft.FragmentoMinecraftContent;
import com.pgalaxyp.fragmento.combat.intentModule.minecraft.IntegratedServerIntentSink;
import java.util.Optional;
import net.minecraft.client.*;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

@EventBusSubscriber(modid = FragmentoPlatform.MODID, value = Dist.CLIENT)
public final class FragmentoClientEvents {
    private static volatile Runtime runtime;

    @SubscribeEvent public static void onKey(InputEvent.InteractionKeyMappingTriggered event) {
        Minecraft instance = Minecraft.getInstance();
        if (instance.level == null) return;
        KeyMapping attack = instance.options.keyAttack;
        if (event.getKeyMapping() != attack) return;
        Runtime r = runtime;
        if (r == null) runtime = r = Runtime.create();
        InputDecision decision = r.primaryHandler.onSemanticInput(SemanticInput.PRIMARY_ACTION);
        if (decision.consumeVanilla()) event.setCanceled(true);
    }

    private record Runtime(PrimaryActionInputHandler primaryHandler) {
        static Runtime create() {
            var weaponBinding = new ItemWeaponBinding();
            for (var e : FragmentoMinecraftContent.REGISTRY.clientWeaponBindings()) weaponBinding.register(e.item(), e.weaponId());
            LocalActorProvider localProvider = () -> {
                LocalPlayer player = Minecraft.getInstance().player;
                return player == null ? Optional.empty() : Optional.of(new ActorId(player.getUUID()));
            };
            ActorInputContextProvider actorContext = new McActorContext(weaponBinding, localProvider);
            IntentSinkPort sink = new IntegratedServerIntentSink(FragmentoMod.INTEGRATED_SERVER_INTENTS::get);
            return new Runtime(new PrimaryActionInputHandler(actorContext, sink));
        }
    }

    private FragmentoClientEvents() {}
}